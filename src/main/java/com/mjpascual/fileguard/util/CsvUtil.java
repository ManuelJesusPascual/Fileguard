package com.mjpascual.fileguard.util;

import com.mjpascual.fileguard.model.ColumnRule;
import com.mjpascual.fileguard.model.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Utility class for CSV data validation operations.
 * Provides methods to validate cell values against column rules including
 * type checking, range validation, and format verification.
 */
@Component
public class CsvUtil {

    /**
     * Validates a cell value against its column rule definition.
     * Performs type checking and constraint validation based on the rule type.
     * Supported types include string, number, integer, and email.
     * For numeric types, also validates min/max constraints if defined.
     *
     * @param value the cell value to validate
     * @param rule the column rule containing type and constraint definitions
     * @param rowNumber the row number in the CSV where this value appears
     * @param column the column name being validated
     * @param errors the list to which validation errors will be added
     */
    public static void validateType(String value, ColumnRule rule, int rowNumber, String column, List<ValidationError> errors) {
        if (value == null || value.isBlank()) {
            return;
        }

        try {
            switch (rule.getType()) {
                case "string":
                    // Always valid
                    break;

                case "number":
                    Double.parseDouble(value);
                    validateMinMax(Double.parseDouble(value), rule, rowNumber, column, errors);
                    break;

                case "integer":
                    Integer.parseInt(value);
                    validateMinMax(Integer.parseInt(value), rule, rowNumber, column, errors);
                    break;

                case "email":
                    if (!isValidEmail(value)) {
                        errors.add(new ValidationError(rowNumber, column, "Invalid email format"));
                    }
                    break;

                default:
                    errors.add(new ValidationError(rowNumber, column, "Not supported type: " + rule.getType()));
            }
        } catch (NumberFormatException e) {
            errors.add(new ValidationError(rowNumber, column, "Incorrect type | Type expected: " + rule.getType()));
        }
    }

    /**
     * Validates if a string matches a basic email format pattern.
     * Uses a simple regex pattern to check for the presence of @ symbol
     * and a domain with at least one dot.
     *
     * @param email the email string to validate
     * @return true if the email matches the expected format, false otherwise
     */
    private static boolean isValidEmail(String email) {
        return email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    }

    /**
     * Validates that a numeric value falls within the min/max range defined in the rule.
     * Adds validation errors if the value is below the minimum or above the maximum.
     *
     * @param value the numeric value to validate
     * @param rule the column rule containing min/max constraints
     * @param rowNumber the row number where this value appears
     * @param column the column name being validated
     * @param errors the list to which range validation errors will be added
     */
    private static void validateMinMax(Number value, ColumnRule rule, int rowNumber, String column, List<ValidationError> errors) {
        if (rule.getMin() != null && value.doubleValue() < rule.getMin()) {
            errors.add(new ValidationError(rowNumber, column, "Value less than the minimum allowed"));
        }

        if (rule.getMax() != null && value.doubleValue() > rule.getMax()) {
            errors.add(new ValidationError(rowNumber, column, "Value greater than the maximum allowed"));
        }
    }


}
