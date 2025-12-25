package com.mjpascual.fileguard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjpascual.fileguard.model.ValidationError;
import com.mjpascual.fileguard.model.ValidationResult;
import com.mjpascual.fileguard.model.ValidationSchema;
import com.mjpascual.fileguard.service.CsvValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 *
 * This controller provides endpoints for validating uploaded files (currently CSV)
 * against user-defined schemas. It handles multipart form data requests containing
 * both the file to validate and a JSON schema definition.
 *
 */
@RestController
@RequestMapping("/api/")
public class ValidationController {

    @Autowired
    CsvValidationService csvValidationService;

    /**
     * Validates an uploaded CSV file against a provided schema.
     * This endpoint accepts a multipart form data request containing:
     *
     * - A CSV file to validate
     * - A JSON schema defining validation rules (required fields, data types, etc.)
     * The method performs the following validations:
     * - Checks that the uploaded file has a .csv extension
     * - Parses and validates the schema JSON structure
     * - Delegates to the validation service for detailed content validation
     *
     * @param file the CSV file to validate, uploaded as multipart/form-data
     * @param schemaJson the validation schema as a JSON string, defining rules and constraints
     * @return a ValidationResult object containing validation status and any detected errors
     * @throws JsonProcessingException if the schema JSON cannot be parsed
     */
    @PostMapping(value = "/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ValidationResult validate(
            @RequestPart("file") MultipartFile file,
            @RequestPart("schema") String schemaJson
    ) {
        ValidationResult result = new ValidationResult();
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
                result.setValid(false);
                result.setErrors(List.of(new ValidationError(0, "file", "Only CSV files are accepted")));
                return result;
            }

            ObjectMapper mapper = new ObjectMapper();
            ValidationSchema schema = mapper.readValue(schemaJson, ValidationSchema.class);

            return csvValidationService.validate(file, schema);

        } catch (JsonProcessingException e) {
            result.setValid(false);
            result.setErrors(List.of(new ValidationError(0, "schema", "Error parsing schema JSON: " + e.getOriginalMessage())));
            return result;
        } catch (Exception e) {
            result.setValid(false);
            result.setErrors(List.of(new ValidationError(0, "internal", "Unexpected error: " + e.getMessage())));
            return result;
        }
    }
}
