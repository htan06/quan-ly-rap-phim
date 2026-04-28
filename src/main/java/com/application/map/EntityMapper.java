package com.application.map;

import java.sql.ResultSet;
import java.util.List;

public interface EntityMapper {
    <T> List<T> mapResultSetToObj(Class<T> clazz, ResultSet resultSet);
}
