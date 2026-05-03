package com.application.dao;

import com.application.dto.user.UpdateStaffInfoDTO;
import com.application.entity.Role;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserDao {
    private final Connection connectionDB;
    private final RoleDao roleDao;

    public void createUser(User user) {
        String sql = "INSERT INTO users (first_name, last_name, email, phone_number, status, username, password) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            connectionDB.setAutoCommit(false);

            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getStatus().name());
            statement.setString(6, user.getUsername());
            statement.setString(7, user.getPassword());

            int rowUpdated = statement.executeUpdate();
            if (rowUpdated == 0) {
                throw new RuntimeException("Tao user khong thanh cong");
            }

            ResultSet rows = statement.getGeneratedKeys();
            rows.next();

            long userId = rows.getLong(1);
            int roleId = roleDao.findRoleByName(user.getRole().getRoleName())
                            .orElseThrow(() -> new RuntimeException("Role not found"))
                        .getId();
            setRole(userId, roleId);

            connectionDB.commit();
        } catch (SQLException e) {
            try {
                connectionDB.rollback();
            } catch (SQLException ignore) {}
            throw new RuntimeException("tao user khong thanh cong");
        } finally {
            try {
                connectionDB.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public Optional<User> findById(Long id) {
        String sql = "SELECT " +
                "u.id, u.first_name, u.last_name, u.email, u.phone_number, u.username, u.password, u.status, r.role_name, u.created_at, u.updated_at " +
                "FROM users as u " +
                "JOIN roles as r " +
                "ON u.role_id = r.id " +
                "WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            List<User> users = mapResultToObj(resultSet);

            resultSet.close();
            return (users.isEmpty()) ? Optional.empty() : Optional.of(users.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT " +
                "u.id, u.first_name, u.last_name, u.email, u.phone_number, u.username, u.password, u.status, r.role_name, u.created_at, u.updated_at " +
                "FROM users as u " +
                "JOIN roles as r " +
                "ON u.role_id = r.id;";

        try (Statement statement = connectionDB.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            return mapResultToObj(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByUserName(String username) {
        String sql = "SELECT " +
                        "u.id, u.first_name, u.last_name, u.email, u.phone_number, u.username, u.password, u.status, r.role_name, u.created_at, u.updated_at " +
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
        String sql ="UPDATE users SET first_name = ?, last_name = ?, email = ?, phone_number, updated_at = GETDATE() = ? WHERE username = ?";

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
                throw new RuntimeException("set role cho user khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> mapResultToObj(ResultSet rows) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rows.next()) {
            users.add(
                    User.builder()
                            .id(rows.getLong("id"))
                            .firstName(rows.getString("first_name"))
                            .lastName(rows.getString("last_name"))
                            .email(rows.getString("email"))
                            .phoneNumber(rows.getString("phone_number"))
                            .username(rows.getString("username"))
                            .password(rows.getString("password"))
                            .status(UserStatus.valueOf(rows.getString("status")))
                            .role(Role.builder()
                                    .roleName(rows.getString("role_name"))
                                    .build())
                            .createdAt(rows.getTimestamp("created_at"))
                            .updatedAt(rows.getTimestamp("updated_at"))
                            .build()
            );
        }

        return users;
    }
}
