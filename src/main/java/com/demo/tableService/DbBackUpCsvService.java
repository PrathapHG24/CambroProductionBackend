package com.demo.tableService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DbBackUpCsvService {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public DbBackUpCsvService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void exportDataAsCsv(String table, String outputFile) throws IOException {
        if (!StringUtils.hasText(table) || !StringUtils.hasText(outputFile)) {
            throw new IllegalArgumentException("Table and output file must be provided.");
        }

        String query = "SELECT * FROM " + table;
        List<Map<String, Object>> data = jdbcTemplate.queryForList(query);

        // Extract column names
        List<String> columnNames = jdbcTemplate.queryForList(
            "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?",
            String.class, table);

        // Write data to CSV file
        try (FileWriter writer = new FileWriter(outputFile)) {
            // Write header row
            for (int i = 0; i < columnNames.size(); i++) {
                if (i > 0) {
                    writer.append(',');
                }
                writer.append(columnNames.get(i));
            }
            writer.append('\n');

            // Write data rows
            for (Map<String, Object> row : data) {
                boolean firstColumn = true;
                for (String columnName : columnNames) {
                    if (!firstColumn) {
                        writer.append(',');
                    }
                    Object value = row.get(columnName);
                    writer.append(value != null ? value.toString() : "");
                    firstColumn = false;
                }
                writer.append('\n');
            }
        }
    }
}

