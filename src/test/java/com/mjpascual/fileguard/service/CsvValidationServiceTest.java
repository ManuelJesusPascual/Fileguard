package com.mjpascual.fileguard.service;

import com.mjpascual.fileguard.model.ColumnRule;
import com.mjpascual.fileguard.model.SchemaOptions;
import com.mjpascual.fileguard.model.ValidationResult;
import com.mjpascual.fileguard.model.ValidationSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CsvValidationServiceTest {

    @Autowired
    private CsvValidationService csvValidationService;

    private ValidationSchema schema;

    @BeforeEach
    void setUp() {
        ColumnRule emailRule = new ColumnRule();
        emailRule.setRequired(true);
        emailRule.setType("email");

        ColumnRule priceRule = new ColumnRule();
        priceRule.setRequired(true);
        priceRule.setType("number");
        priceRule.setMin(0);

        Map<String, ColumnRule> columns = new HashMap<>();
        columns.put("email", emailRule);
        columns.put("price", priceRule);

        SchemaOptions options = new SchemaOptions();
        options.setAllowEmptyRows(false);
        options.setAllowDuplicates(true);

        schema = new ValidationSchema();
        schema.setColumns(columns);
        schema.setOptions(options);
    }

    @Test
    void testCsvCorrecto() throws IOException {
        String csv = "email,price\n" +
                     "test@test.com,10\n" +
                     "hola@hola.com,5";
        MultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", csv.getBytes()
        );

        ValidationResult result = csvValidationService.validate(file, schema);

        Assertions.assertTrue(result.isValid());
        Assertions.assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void testCsvConErrores() throws IOException {
        String csv = "email,price\n" +
                     "bademail,-5\n" +
                     ",10";
        MultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", csv.getBytes()
        );

        ValidationResult result = csvValidationService.validate(file, schema);

        Assertions.assertFalse(result.isValid());
        Assertions.assertEquals(3, result.getErrors().size());
        Assertions.assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.getColumn().equals("email")));
        Assertions.assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.getColumn().equals("price")));
    }
}
