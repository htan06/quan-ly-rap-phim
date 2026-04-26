package com.application.map;

import java.lang.reflect.Field;

public class FieldMetadata {
    private String columnName;
    private Field field;

    public FieldMetadata(String columnName, Field field) {
        this.columnName = columnName;
        this.field = field;
    }

    public String getColumnName() {
        return columnName;
    }

    public Field getField() {
        return field;
    }
}
