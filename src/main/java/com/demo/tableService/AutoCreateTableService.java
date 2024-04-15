package com.demo.tableService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class AutoCreateTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String createTable(String dbName, String tableName, String jsonString) {
        try {
            if (tableExists(dbName, tableName)) {
                return "Table " + tableName + " already exists in database " + dbName;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            if (jsonNode.isArray() && jsonNode.size() > 0) {
                JsonNode firstObject = jsonNode.get(0);

                if (firstObject.isObject()) {
                    Iterator<String> fieldNames = firstObject.fieldNames();
                    StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
                    createTableSQL.append(dbName).append(".").append(tableName).append(" (");

                    while (fieldNames.hasNext()) {
                        String fieldName = fieldNames.next();
                        JsonNode fieldValue = firstObject.get(fieldName);
                        String dataType = inferDataType(fieldValue);
                        createTableSQL.append(fieldName).append(" ").append(dataType).append(", ");
                    }

                    // Add additional columns for start time, end time, and date
                    createTableSQL.append("startTime DATETIME, ");
                    createTableSQL.append("endTime DATETIME, ");
                    createTableSQL.append("date DATE, ");

                    // Remove the trailing comma and space
                    createTableSQL.setLength(createTableSQL.length() - 2);

                    // Add primary key and closing parenthesis
                    createTableSQL.append(");");

                    jdbcTemplate.execute(createTableSQL.toString());

                    return "Table created successfully";
                } else {
                    return "Invalid JSON format";
                }
            } else {
                return "No JSON data found";
            }
        } catch (Exception e) {
            return "Error creating table: " + e.getMessage();
        }
    }

    private boolean tableExists(String dbName, String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, dbName, tableName);
        return count > 0;
    }

    private String inferDataType(JsonNode fieldValue) {
        if (fieldValue.isInt() || fieldValue.isLong()) {
            return "INT";
        } else if (fieldValue.isDouble() || fieldValue.isFloat()) {
            return "DOUBLE";
        } else if (fieldValue.isTextual()) {
            String value = fieldValue.textValue();
            if (value.matches("\\d{4}-\\d{2}-\\d{2}")) { // Check if it's a date in yyyy-MM-dd format
                return "DATE";
            } else if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) { // Check if it's a datetime in yyyy-MM-dd HH:mm:ss format
                return "DATETIME";
            } else if (value.matches("\\d{2}:\\d{2}:\\d{2}")) { // Check if it's a time in HH:mm:ss format
                return "TIME";
            }
        }
        return "VARCHAR(255)";
    }
}