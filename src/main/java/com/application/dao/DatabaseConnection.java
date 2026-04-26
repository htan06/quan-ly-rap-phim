package com.application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url = "jdbc:sqlserver://localhost:1433;databasename=Movies;encrypt=true;trustServerCertificate=true;";
    private final String user = "sa";
    private final String password = "12345678";

    private Connection connection;
    private static DatabaseConnection databaseConnection = new DatabaseConnection();

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null) new DatabaseConnection();
        return databaseConnection;
    }

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
