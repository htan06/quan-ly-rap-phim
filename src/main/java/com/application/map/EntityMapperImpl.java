package com.application.map;

import com.application.map.annotation.Column;
import com.application.map.annotation.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EntityMapperImpl implements EntityMapper {

    private static EntityMapper entityMapper = new EntityMapperImpl();
    private Map<String, EntityMetaData> entityMetaData;

    public static EntityMapper getInstance() {
        return entityMapper;
    }

    private EntityMapperImpl() {
        entityMetaData = new HashMap<>();
    }

    private void registry(Class<?> clazz) {
        if (clazz == null || entityMetaData.containsKey(clazz.getSimpleName()) || clazz.getDeclaredAnnotation(Entity.class) == null) return;

        String entityName = clazz.getSimpleName();
        List<FieldMetadata> fieldMetadata = new ArrayList<>();

        while (clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                Column c = f.getDeclaredAnnotation(Column.class);
                if (c == null) continue;
                f.setAccessible(true);
                fieldMetadata.add(new FieldMetadata(c.name(), f));
            }
            clazz = clazz.getSuperclass();
        }

        EntityMetaData metaData = new EntityMetaData(entityName, fieldMetadata);

        entityMetaData.put(entityName, metaData);
    }

    @Override
    public <T> List<T> mapResultSetToObj(Class<T> clazz, ResultSet resultSet) {
        EntityMetaData metaData = entityMetaData.get(clazz.getSimpleName());
        if (metaData == null) {
            registry(clazz);
            metaData = entityMetaData.get(clazz.getSimpleName());
        }

        if (resultSet == null) {
            throw new RuntimeException("Result set null");
        }

        List<T> listData = new ArrayList<>();

        try {
            Constructor<T> con = clazz.getDeclaredConstructor();

            while (resultSet.next()) {
                T target = con.newInstance();

                for (FieldMetadata fieldMetadata : metaData.getFieldMetaData()) {
                    Field field = fieldMetadata.getField();

                    if (field.getType().equals(UUID.class)) {
                        byte[] id = resultSet.getBytes("id");
                        field.set(target, UUID.nameUUIDFromBytes((byte[]) id));
                    } else if (field.getType().isEnum()) {
                        String enumValue = resultSet.getString(fieldMetadata.getColumnName());
                        field.set(target, Enum.valueOf((Class<Enum>) field.getType(), enumValue));
                    } else {
                        Object value = resultSet.getObject(fieldMetadata.getColumnName());
                        field.set(target, value);
                    }
                }
                listData.add(target);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 SQLException e) {
            e.printStackTrace();
        }

        return listData;
    }
}
