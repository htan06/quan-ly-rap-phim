package com.application.map;

import java.util.List;

public class EntityMetaData {
    private String tableName;
    private List<FieldMetadata> fieldMetaData;

    public EntityMetaData(String tableName, List<FieldMetadata> fieldMetaData) {
        if (tableName == null || tableName.trim().isBlank()) {
            throw new IllegalArgumentException("Metadata entity khong phu hop");
        }

        this.tableName = tableName;
        this.fieldMetaData = fieldMetaData;
    }

    public String getTableName() {
        return tableName;
    }

    public List<FieldMetadata> getFieldMetaData() {
        return fieldMetaData;
    }
}
