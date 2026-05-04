package com.application.dao;

import com.application.entity.Room;
import com.application.entity.enums.RoomStatus;
import com.application.entity.enums.RoomType;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RoomDao {
    private final Connection connectionDB;

    public List<Room> findAll() {
        String sql = "SELECT id, name, room_type, capacity, status, created_at, updated_at FROM rooms ORDER BY id DESC";
        List<Room> rooms = new ArrayList<>();

        try (Statement statement = connectionDB.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                rooms.add(Room.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .roomType(RoomType.valueOf(rs.getString("room_type")))
                        .capacity(rs.getInt("capacity"))
                        .status(RoomStatus.valueOf(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))
                        .updatedAt(rs.getTimestamp("updated_at"))
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn danh sách phòng", e);
        }
        return rooms;
    }
}