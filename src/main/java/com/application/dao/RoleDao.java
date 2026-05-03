package com.application.dao;

import com.application.entity.Role;
import com.application.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDao {
    private Connection connectionDB;

    public RoleDao(Connection connectionDB) {
        this.connectionDB = connectionDB;
    }

    public void createRole(Role role) {
        String sql = "INSERT INTO roles (role_name) VALUES (?);";
        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, role.getRoleName());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Tao user khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Role findRoleByName(String name) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Role.builder()
                        .id(resultSet.getInt("id"))
                        .roleName("role_name")
                        .build();
            } else {
                throw new IllegalArgumentException("Role not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
