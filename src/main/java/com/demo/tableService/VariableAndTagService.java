package com.demo.tableService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

     @Service
    public class VariableAndTagService {

    private static final Logger logger = LoggerFactory.getLogger(VariableAndTagService.class);
    
        @Autowired
    private JdbcTemplate jdbcTemplate;
   ////this is to save jsonVariable
    // public List<String> saveJsonKeyVariables(List<Map<String, Object>> dataList) throws SQLException {
    //     String dbName = "mold1";
    //     String tableName = "variableandplctag";
    //     String columnName="jsonVariable";
    //     List<String> insertedKeys = new ArrayList<>();
    //     try {
    //         for (Map<String, Object> data : dataList) {
    //             // Extract keys from the JSON object
    //             Set<String> keys = data.keySet();

    //             for (String key : keys) {
    //                 // Construct SQL query to insert key into the table
    //                 String sql = String.format("INSERT INTO %s.%s (jsonVariable) VALUES (?)", dbName, tableName);

    //                 // Bind key and execute query
    //                 jdbcTemplate.update(sql, key);

    //                 logger.info("Key '{}' inserted successfully into table '{}'", key, tableName);
    //                 insertedKeys.add(key);
    //             }
    //         }
    //         return insertedKeys;
    //     } catch (SQLGrammarException e) {
    //         logger.error("SQL Error occurred while inserting keys into table '{}': {}", tableName, e.getMessage(), e);
    //         throw e; // Propagate the exception back to the controller
    //     } catch (Exception e) {
    //         logger.error("Error occurred while inserting keys into table '{}': {}", tableName, e.getMessage(), e);
    //         throw e; // Propagate the exception back to the controller
    //     }
    // }
    public List<String> saveJsonKeyVariables(List<Map<String, Object>> dataList) throws SQLException {
        String dbName = "mold1";
        String tableName = "variableandplctag";
        String keyColumnName = "jsonVariable";
        String valueColumnName = "plcTag";
        List<String> insertedKeys = new ArrayList<>();
        try {
            for (Map<String, Object> data : dataList) {
                // Extract keys and values from the map
                Set<String> keys = data.keySet();
    
                for (String key : keys) {
                    Object value = data.get(key);
    
                    // Check if the key exists in the database
                    String selectSql = String.format("SELECT COUNT(*) FROM %s.%s WHERE %s = ?", dbName, tableName, keyColumnName);
                    int count = jdbcTemplate.queryForObject(selectSql, Integer.class, key);
                    
                    if (count == 0) {
                        // Key does not exist in the database, insert it
                        String insertSql = String.format("INSERT INTO %s.%s (%s, %s) VALUES (?, ?)", dbName, tableName, keyColumnName, valueColumnName);
                        jdbcTemplate.update(insertSql, key, value);
                        insertedKeys.add(key);
                    } else {
                        // Key exists in the database, update its value
                        String updateSql = String.format("UPDATE %s.%s SET %s = ? WHERE %s = ?", dbName, tableName, valueColumnName, keyColumnName);
                        jdbcTemplate.update(updateSql, value, key);
                    }
                    logger.info("Key '{}' with value '{}' inserted/updated successfully into table '{}'", key, value, tableName);
                }
            }
            return insertedKeys;
        } catch (SQLGrammarException e) {
            logger.error("SQL Error occurred while inserting/updating keys and values into table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        } catch (Exception e) {
            logger.error("Error occurred while inserting/updating keys and values into table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        }
    }
    

    //to read the available jsonVariables and plcTags
    public List<Map<String, Object>> getAllDataFromTable() throws SQLException {
        String dbName = "mold1";
        String tableName = "variableandplctag";
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
            // Construct SQL query to select all data from the table
            String sql = String.format("SELECT * FROM %s.%s", dbName, tableName);

            // Execute query and retrieve results
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            // Process each row and add to dataList
            for (Map<String, Object> row : rows) {
                dataList.add(new HashMap<>(row)); // Create a copy to avoid modifying the original map
            }

            logger.info("Retrieved all data from table '{}'", tableName);
            return dataList;
        } catch (Exception e) {
            logger.error("Error occurred while retrieving data from table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the caller
        }
    }

    ///this to update the plcTag 
    public void updatePlcTagBasedOnJsonVariable(String jsonVariable, String PlcTag) throws SQLException {
        String dbName = "mold1";
        String tableName = "variableandplctag";
        try {
            // Construct SQL query to update plcTag based on jsonVariable
            String sql = String.format("UPDATE %s.%s SET plcTag = ? WHERE jsonVariable = ?", dbName, tableName);

            // Bind newPlcTag and jsonVariable and execute update
            jdbcTemplate.update(sql, PlcTag, jsonVariable);

            logger.info("plcTag updated successfully in table '{}' for jsonVariable '{}'", tableName, jsonVariable);
        } catch (SQLGrammarException e) {
            logger.error("SQL Error occurred while updating plcTag in table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        } catch (Exception e) {
            logger.error("Error occurred while updating plcTag in table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        }
    }

        
} 

