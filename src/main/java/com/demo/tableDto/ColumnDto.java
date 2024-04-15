package com.demo.tableDto;
//This is use in createTableDto class 
    public class ColumnDto {
        private String name;
        private String datatype;
        private Integer length;
        private boolean notNull;
        private boolean autoIncrement;
        private String defaultValue;
    
    public String getName() {
        return name;
    }
     public void setName(String name) {
        this.name = name;
    }
    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
   
    public Integer getLength() {
        return length;
    }
    
    public void setLength(Integer length) {
        this.length = length;
    }

    public boolean isNotNull() {
        return notNull;
    }
    
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
    
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
    
    

