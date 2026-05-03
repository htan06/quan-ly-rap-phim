package com.application.dao;

import com.application.entity.Role;
import com.application.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class RoleDao {
    private final Connection connectionDB;

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

    public Optional<Role> findRoleByName(String name) {
        String sql = "SELECT * FROM roles WHERE role_name = ?";
        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, name);

            ResultSet rows = statement.executeQuery();

            if (rows.next()) {
                return Optional.of(
                        Role.builder()
                            .id(rows.getInt("id"))
                            .roleName("role_name")
                            .createdAt(rows.getTimestamp("created_at"))
                            .updatedAt(rows.getTimestamp("updated_at"))
                            .build()
                        );
            } else {
                throw new IllegalArgumentException("Role not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
