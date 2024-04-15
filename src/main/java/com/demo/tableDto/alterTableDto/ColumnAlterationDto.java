package com.demo.tableDto.alterTableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnAlterationDto {
    private String columnName;
    private String newColumnName; // For renaming columns
    private String newColumnType; // For changing data type
    private Integer newColumnLength; // For modifying length
    private Boolean dropColumn; // If true, drop the column

    // Getters and setter
}