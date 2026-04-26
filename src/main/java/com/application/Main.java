package com.application;

import com.application.dao.DatabaseConnection;
import com.application.model.User;
import com.application.map.EntityMapper;
import com.application.map.EntityMapperImpl;
import com.application.model.enums.UserStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        EntityMapper entityMapper = EntityMapperImpl.getInstance();
        try {
        Connection connection = DatabaseConnection.getInstance().getConnection();

            System.out.println("Connected !!");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users");


            List<User> users = entityMapper.mapResultSetToObj(User.class, resultSet);

            users.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}