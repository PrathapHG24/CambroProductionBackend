package com.demo.tableDto.alterTableDto;

public class AddColumnDto {
    private String tableName;
    private String columnName;
    private String columnType;
    private Integer length;

    // Getters and setters...
   
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }
    
    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getLength() {
        return length;
    }
    
    public void setLength(Integer length) {
        this.length = length;
    }

}