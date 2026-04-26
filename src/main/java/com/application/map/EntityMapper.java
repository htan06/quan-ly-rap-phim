package com.application.map;

import java.sql.ResultSet;
import java.util.List;

/*
* Quản lý các entity.
* */
public interface EntityMapper {

    // Thực hiện truy vấn với tham số đầu là entity, tham số thứ hai câu query
    <T> List<T> mapResultSetToObj(Class<T> clazz, ResultSet resultSet);
}
