package com.demo.tableDto.alterTableDto;

public class RenameTableDto {
    private String oldTableName;
    private String newTableName;

    // Getters and setters...

    public String getOldTableName() {
        return oldTableName;
    }

    public void setOldTableName(String oldTableName) {
        this.oldTableName = oldTableName;
    }

    public String getNewTableName() {
        return newTableName;
    }

    public void setNewTableName(String newTableName) {
        this.newTableName = newTableName;
    }

}