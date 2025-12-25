package com.mjpascual.fileguard.service;

import com.mjpascual.fileguard.model.ColumnRule;
import com.mjpascual.fileguard.model.ValidationError;
import com.mjpascual.fileguard.model.ValidationResult;
import com.mjpascual.fileguard.model.ValidationSchema;
import com.mjpascual.fileguard.util.CsvParser;
import com.mjpascual.fileguard.util.CsvUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;


/**
 * Service for validating CSV files against defined schemas.
 * This service processes uploaded CSV files, applies validation rules from the schema,
 * and returns detailed error reports for any violations found.
 */
@Service
public class CsvValidationService {
    /**
     * Validates a CSV file against a provided validation schema.
     * The method parses the CSV file, iterates through each row, and checks each column
     * against the rules defined in the schema. Validation includes checking for required
     * fields and data type conformity.
     *
     * @param file the multipart file containing the CSV data to validate
     * @param schema the validation schema containing column rules and constraints
     * @return a ValidationResult containing the validation status and list of errors found
     * @throws RuntimeException if the CSV file is empty
     */
    public ValidationResult validate(MultipartFile file, ValidationSchema schema) {

        List<Map<String, String>> rows = CsvParser.parse(file);

        if(rows.isEmpty()){
            throw new RuntimeException("Empty CSV");
        }
        if(schema.getColumns() == null || isEmpty(schema.getColumns())){
            throw new RuntimeException("Empty CSV");
        }

        int rowNumber = 1; //Header is 0
        List<ValidationError> errors = new ArrayList<>();

        for (Map<String, String> row : rows) {
            for (Map.Entry<String, ColumnRule> entry : schema.getColumns().entrySet()) {
                String column = entry.getKey();
                ColumnRule rule = entry.getValue();

                String value = row.get(column);

                // required
                if (rule.isRequired() && (value == null || value.isBlank())) {
                    errors.add(new ValidationError(rowNumber, column, "Required field is empty"));
                    continue;
                }

                // type validation
               CsvUtil.validateType(value, rule, rowNumber, column, errors);
                rowNumber++;
            }
        }

        return new ValidationResult(errors.isEmpty(), errors);


    }


}
