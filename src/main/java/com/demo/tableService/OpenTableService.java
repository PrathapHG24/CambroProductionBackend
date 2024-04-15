package com.demo.tableService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class OpenTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getTableData(String dbName, String tableName) {
        // You should validate dbName and tableName to prevent SQL Injection
        // For example, check against a list of allowed database and table names

        // Get table metadata (columns)
        List<Map<String, Object>> columns = getTableColumns(dbName, tableName);

        // Fetch table data
        String sql = "SELECT * FROM " + dbName + "." + tableName + ";";
        List<Map<String, Object>> tableData = jdbcTemplate.queryForList(sql);

        // If no data exists, construct a result with only column names
        if (tableData.isEmpty()) {
            return columns;
        } else {
            return tableData;
        }
    }

    private List<Map<String, Object>> getTableColumns(String dbName, String tableName) {
        String sql = "SELECT * FROM information_schema.columns " +
                     "WHERE table_schema = ? AND table_name = ?";
        return jdbcTemplate.queryForList(sql, dbName, tableName);
    }
}