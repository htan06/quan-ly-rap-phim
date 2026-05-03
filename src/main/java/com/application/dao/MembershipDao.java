package com.application.dao;

import com.application.entity.Membership;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MembershipDao {

    private final Connection connectionDB;

    public Long create(Membership membership) {
        String sql = "INSERT INTO memberships (name, phone_number, member_point) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, membership.getName());
            statement.setString(2, membership.getPhoneNumber());
            statement.setInt(3, membership.getMemberPoint());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Tao membership khong thanh cong");
            }

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new RuntimeException("Tao membership khong thanh cong", e);
        }
    }

    public List<Membership> findAll() {
        String sql = "SELECT * FROM memberships";

        try (Statement statement = connectionDB.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return mapResultToObj(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Membership> findById(Long id) {
        String sql = "SELECT * FROM memberships WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            List<Membership> list = mapResultToObj(rs);

            return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Membership> findByPhone(String phoneNumber) {
        String sql = "SELECT * FROM memberships WHERE phone_number = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setString(1, phoneNumber);

            ResultSet rs = statement.executeQuery();
            List<Membership> list = mapResultToObj(rs);

            return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(Membership membership) {
        String sql = "UPDATE memberships SET " +
                "name = ?, " +
                "phone_number = ?, " +
                "updated_at = GETDATE() " +
                "WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setString(1, membership.getName());
            statement.setString(2, membership.getPhoneNumber());
            statement.setLong(3, membership.getId());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Cap nhat membership khong thanh cong");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePoint(Long id, int point) {
        String sql = "UPDATE memberships SET " +
                "member_point = ?, " +
                "updated_at = GETDATE() " +
                "WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setInt(1, point);
            statement.setLong(2, id);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Cap nhat point khong thanh cong");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Membership> mapResultToObj(ResultSet rs) throws SQLException {
        List<Membership> list = new ArrayList<>();

        while (rs.next()) {
            list.add(
                    Membership.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .phoneNumber(rs.getString("phone_number"))
                            .memberPoint(rs.getInt("member_point"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build()
            );
        }

        return list;
    }
}