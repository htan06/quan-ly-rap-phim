package com.application.dao;

import com.application.map.EntityMapper;
import com.application.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserDao {
    private Connection connectionDB;
    private EntityMapper entityMapper;

    public UserDao(Connection connectionDB, EntityMapper entityMapper) {
        this.connectionDB = connectionDB;
        this.entityMapper = entityMapper;
    }

    public List<User> getListUser() {
        try {
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            List<User> users = entityMapper.mapResultSetToObj(User.class, resultSet);
            resultSet.close();
            statement.close();

            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
