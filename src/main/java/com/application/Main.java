package com.application;

import com.application.dao.DatabaseConnection;
import com.application.dao.UserDao;
import com.application.model.User;
import com.application.map.EntityMapper;
import com.application.map.EntityMapperImpl;
import com.application.model.enums.UserStatus;
import com.application.service.UserService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        EntityMapper entityMapper = EntityMapperImpl.getInstance();
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        UserDao userDao = new UserDao(databaseConnection.getConnection(), entityMapper);
        UserService userService = new UserService(userDao);

        userService.printUsers();
    }
}