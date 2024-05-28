package com.demo.mappingService;

import java.sql.SQLException;

import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.demo.service.UserEventService;

import java.time.LocalDateTime;
import java.util.*;

import javax.transaction.Transactional;
@Service
public class MappingService {
    private static final Logger logger = LoggerFactory.getLogger(MappingService.class);
    @Autowired
    UserEventService userEventService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public MappingService(JdbcTemplate jdbcTemplate){
    this.jdbcTemplate = jdbcTemplate;
     }
    public List<Map<String, Object>> insertData(String dbName, String tableName, List<Map<String, Object>> dataList) throws SQLException {
         MappingService mapingService = new MappingService(jdbcTemplate);
         mapingService.saveJsonKeyVariables1(dataList);
       
        List<Map<String, Object>> insertedDataList = new ArrayList<>();
        try {
            for (Map<String, Object> data : dataList) {
                // Add starttime, endtime, and date to the data map
                LocalDateTime currentTime = LocalDateTime.now();
                data.put("starTtime", currentTime);
                data.put("endTime", null); // Assuming endtime is initially null
                data.put("date", currentDate());
    
                // Construct SQL query using placeholders
                String sql = String.format("INSERT INTO %s.%s (%s) VALUES (%s)",
                        dbName, tableName,
                        String.join(", ", data.keySet()),
                        String.join(", ", Collections.nCopies(data.size(), "?")));
    
                // Bind values and execute query
                Object[] values = data.values().toArray();
                jdbcTemplate.update(sql, values); // Pass only the data values array
                logger.info("Data inserted successfully into table '{}'", tableName);
                insertedDataList.add(data);
            }
            return insertedDataList;
        } catch (SQLGrammarException e) {
            logger.error("SQL Error occurred while inserting data into table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        } catch (Exception e) {
            logger.error("Error occurred while inserting data into table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        }
    }
    
    // Helper method to get current date
    private java.sql.Date currentDate() {
        return new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }
    

    @Transactional
    public String updateEndTime(String dbName, String tableName, String scheduleID) {
        try {
            // Generate current timestamp
            LocalDateTime endTime = LocalDateTime.now();

            // Construct the SQL query
            String sql = String.format("UPDATE %s.%s SET endTime = ? WHERE Schedule_Olsn_Release_Barcode = ?", dbName, tableName);

            // Execute the update query
            int rowsAffected = jdbcTemplate.update(sql, endTime, scheduleID);

            if (rowsAffected > 0) {
                return "End time updated successfully";
            } else {
                throw new RuntimeException("No rows affected. please check the entered scheduleId.");
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an error message
            return "Failed to update end time: " + e.getMessage();
        }
    }
     
    public List<String> saveJsonKeyVariables1(List<Map<String, Object>> dataList) throws SQLException {
        String dbName = "mold1";
        String tableName = "variableandplctag";
        String keyColumnName = "jsonVariable";
        List<String> insertedKeys = new ArrayList<>();
        try {
            // Check if the table is empty
            String countSql = String.format("SELECT COUNT(*) FROM %s.%s", dbName, tableName);
            int rowCount = jdbcTemplate.queryForObject(countSql, Integer.class);
    
            if (rowCount == 0) {
                // Table is empty, insert keys directly
                for (Map<String, Object> data : dataList) {
                    for (String key : data.keySet()) {
                        String insertSql = String.format("INSERT INTO %s.%s (%s) VALUES (?)", dbName, tableName, keyColumnName);
                        jdbcTemplate.update(insertSql, key);
                        insertedKeys.add(key);
                        logger.info("Key '{}' inserted successfully into table '{}'", key, tableName);
                    }
                }
            } else {
                // Table has data, proceed with checking and inserting keys
                for (Map<String, Object> data : dataList) {
                    // Extract keys from the map
                    Set<String> keys = data.keySet();
    
                    for (String key : keys) {
                        // Check if the key exists in the database
                        String selectSql = String.format("SELECT COUNT(*) FROM %s.%s WHERE %s = ?", dbName, tableName, keyColumnName);
                        int count = jdbcTemplate.queryForObject(selectSql, Integer.class, key);
                        
                        if (count == 0) {
                            // Key does not exist in the database, insert it
                            String insertSql = String.format("INSERT INTO %s.%s (%s) VALUES (?)", dbName, tableName, keyColumnName);
                            jdbcTemplate.update(insertSql, key);
                            insertedKeys.add(key);
                            logger.info("Key '{}' inserted successfully into table '{}'", key, tableName);
                        }
                    }
                }
            }
            return insertedKeys;
        } catch (SQLGrammarException e) {
            logger.error("SQL Error occurred while inserting keys into table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        } catch (Exception e) {
            logger.error("Error occurred while inserting keys into table '{}': {}", tableName, e.getMessage(), e);
            throw e; // Propagate the exception back to the controller
        }
    }
    
    
    
    // scheduleId Save feature
    public String saveScheduleID(String scheduleID) {
        try {
        userEventService.updateWithScheduleId(scheduleID);
        // String tableName = "mold1.scheduleid";
        // String sql = String.format("INSERT INTO %s (scheduleID) VALUES (?)", tableName);

        // // Execute the query using JdbcTemplate
        // jdbcTemplate.update(sql, scheduleID);
        // logger.info("Updated scheduleId {} Saved Successfully {}", scheduleID);
           return "scheduleId Saved Successfully";
    } catch (DataAccessException e) {
        logger.error("Error in saving scheduleId {} in database {}: {}", e.getMessage(), e);
        return "Error in Saving the dat: " + e.getMessage();
    }

    }
   //to get the scheduleID
    public List<Map<String, String>> getAllScheduleIDs() {
        //String tableName = "scheduledb.scheduleid";
        String sql = "SELECT scheduleID FROM mold1.scheduleid";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, String> scheduleId = new HashMap<>();
            scheduleId.put("name", rs.getString(1));
            return scheduleId;
        });
    }
}