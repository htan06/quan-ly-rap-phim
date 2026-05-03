package com.application.dao;

import com.application.dto.UpdateStaffInfoDTO;
import com.application.entity.Role;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private Connection connectionDB;

    public UserDao(Connection connectionDB) {
        this.connectionDB = connectionDB;
    }

    public void createUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone_number, status, username, password) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getStatus().name());
            statement.setString(6, user.getUsername());
            statement.setString(7, user.getPassword());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Tao user khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByUserName(String username) {
        String sql = "SELECT first_name, last_name, email, phone_number, username, password, role_name " +
                    "FROM users as u " +
                    "JOIN roles as r " +
                        "ON u.role_id = r.id " +
                    "WHERE username = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            List<User> users = mapResultToObj(resultSet);

            resultSet.close();
            return (users.isEmpty()) ? Optional.empty() : Optional.of(users.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changePassword(String username, String newHashPassword) {
        String sql = "UPDATE users SET password = ?, updated_at = GETDATE() WHERE username = ?";
        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, newHashPassword);
            statement.setString(2, username);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Doi mat khau khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(UpdateStaffInfoDTO updateStaffInfo) {
        String sql ="UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number = ? WHERE username = ?, updated_at = GETDATE()";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, updateStaffInfo.firstName());
            statement.setString(2, updateStaffInfo.lastName());
            statement.setString(3, updateStaffInfo.email());
            statement.setString(4, updateStaffInfo.phoneNumber());
            statement.setString(5, updateStaffInfo.username());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Cap nhat thong tin user khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(String username, UserStatus status) {
        String sql = "UPDATE users SET status = ?, updated_at = GETDATE() WHERE username = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setString(2, username);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("cap nhat status khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRole(Long userId, Integer roleId) {
        String sql = "INSERT INTO user_has_role (user_id, role_id) VALUES (?, ?);";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setInt(2, roleId);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("cap nhat role khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> mapResultToObj(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            users.add(
                    User.builder()
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .email(resultSet.getString("email"))
                            .phoneNumber(resultSet.getString("phone_number"))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password"))
                            .role(Role.builder()
                                    .roleName(resultSet.getString("role_name"))
                                    .build())
                            .build()
            );
        }

        return users;
    }
}
