package com.demo.tableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicTableService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTableService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
	//Delete Database feature
	public String dropDatabase(String dbName) {
        try {
            String sql = "DROP DATABASE " + dbName;
            jdbcTemplate.execute(sql);
            logger.info("Database dropped successfully: {}", dbName);
            return "Database dropped successfully";
        } catch (DataAccessException e) {
            logger.error("Error in dropping database {}: {}", dbName, e.getMessage(), e);
            return "Error in dropping database: " + e.getMessage();
        }
    }
	//delete table feature
	public String dropTable(String dbName, String tableName) {
        try {
            String sql = "DROP TABLE " + dbName + "." + tableName;
            jdbcTemplate.execute(sql);
            logger.info("Table {} dropped successfully from database {}", tableName, dbName);
            return "Table dropped successfully";
        } catch (DataAccessException e) {
            logger.error("Error in dropping table {} from database {}: {}", tableName, dbName, e.getMessage(), e);
            return "Error in dropping table: " + e.getMessage();
        }
    }
	//read the column name from the database table
	public List<Map<String, String>> getTableColumns(String dbName, String tableName) throws DataAccessException {
       
        try {
            String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? ORDER BY ORDINAL_POSITION";
            return jdbcTemplate.query(sql, new Object[]{dbName, tableName}, (rs, rowNum) -> {
                Map<String, String> columnMap = new HashMap<>();
                columnMap.put("columnName", rs.getString("COLUMN_NAME"));
                return columnMap;
            });
        } catch (DataAccessException e) {
            // Log or handle the exception appropriately
            throw e; // rethrowing the exception as it's better to propagate it in this context
        }
    }
}

