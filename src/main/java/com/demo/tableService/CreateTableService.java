package com.demo.tableService;

import com.demo.tableDto.Column;
import com.demo.tableDto.CreateTableDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CreateTableService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTableService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public String createTable(String dbName, CreateTableDto tableRequest) {
        logger.info("Attempting to create table: {}.{}", dbName, tableRequest.getTableName());
        if (doesTableExist(dbName, tableRequest.getTableName())) {
            logger.warn("Table creation aborted: Table {}.{} already exists", dbName, tableRequest.getTableName());
            return "Table already exists";
        }
 

        String sql = buildCreateTableQuery(dbName, tableRequest);
        try {
            jdbcTemplate.execute(sql);
            logger.info("Table {}.{} created successfully", dbName, tableRequest.getTableName());
            return "Table created successfully";
        } catch (Exception e) {
            logger.error("Error in creating table {}.{}: {}", dbName, tableRequest.getTableName(), e.getMessage(), e);
            return "Error in creating table: " + e.getMessage();
        }
    }

    private boolean doesTableExist(String dbName, String tableName) {
        try {
            String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
            List<String> result = jdbcTemplate.query(sql, new Object[]{dbName, tableName}, (rs, rowNum) -> rs.getString("TABLE_NAME"));

            return !result.isEmpty();
        } catch (DataAccessException e) {
            logger.error("Error checking existence of table {}.{}: {}", dbName, tableName, e.getMessage(), e);
            return false;
        }
    }

    private String buildCreateTableQuery(String dbName, CreateTableDto request) {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(dbName).append(".").append(request.getTableName()).append(" (");

        List<Column> columns = request.getColumns();
        StringBuilder primaryKeyColumns = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            sql.append(column.getName())
               .append(" ")
               .append(column.getDataType());

            if (column.getLength() != null) {
                sql.append("(").append(column.getLength()).append(")");
            }

            if ("primarykey".equalsIgnoreCase(column.getKey())) {
                if (primaryKeyColumns.length() > 0) {
                    primaryKeyColumns.append(", ");
                }
                primaryKeyColumns.append(column.getName());
            }

            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }

        if (primaryKeyColumns.length() > 0) {
            sql.append(", PRIMARY KEY (").append(primaryKeyColumns).append(")");
        }

        sql.append(");");
        return sql.toString();
    }

    
}