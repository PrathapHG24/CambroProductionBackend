package com.demo.tableService;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.dao.DataAccessException;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.stereotype.Service;
    
    @Service
    public class CreateDatabaseService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String createDatabase(String databaseName) {
        if (databaseExists(databaseName)) {
            return "Database '" + databaseName + "' already exists.";
        }

        try {
            String sql = "CREATE DATABASE " + databaseName;
            jdbcTemplate.execute(sql);
            return "Database '" + databaseName + "' created successfully.";
        } catch (Exception e) {
            return "Error creating database: " + e.getMessage();
        }
    }

    private boolean databaseExists(String databaseName) {
        try {
            String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
            String result = jdbcTemplate.queryForObject(sql, new Object[]{databaseName}, String.class);
            return result != null;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
