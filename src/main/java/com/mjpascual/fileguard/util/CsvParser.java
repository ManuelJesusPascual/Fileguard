package com.mjpascual.fileguard.util;

import com.opencsv.CSVReaderHeaderAware;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Utility class for parsing CSV files into structured data.
 * This parser uses OpenCSV to read CSV files with headers and converts
 * each row into a map where keys are column names and values are cell contents.
 */
@Component
public class CsvParser {
    /**
     * Parses a CSV file from a multipart file upload into a list of maps.
     * Each map represents a row in the CSV, with column headers as keys and cell values as values.
     * The CSV is read using UTF-8 encoding and the first row is treated as the header.
     *
     * @param file the multipart file containing the CSV data
     * @return a list of maps, where each map represents a row with column-to-value mappings
     * @throws RuntimeException if an error occurs during CSV parsing or file reading
     */
    public static List<Map<String, String>> parse(MultipartFile file) {
        try (
                Reader reader = new BufferedReader(
                        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(reader);

            List<Map<String, String>> rows = new ArrayList<>();
            Map<String, String> row;

            while ((row = csvReader.readMap()) != null) {
                rows.add(row);
            }

            return rows;

        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV", e);
        }
    }

}
