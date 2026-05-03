package com.application.dao;

import com.application.entity.Room;
import com.application.entity.enums.RoomStatus;
import com.application.entity.enums.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDao {

    private Connection connectionDB;
    private SeatDao seatDao;

    public RoomDao(Connection connectionDB, SeatDao seatDao) {
        this.connectionDB = connectionDB;
        this.seatDao = seatDao;
    }

    public Integer createRoom(Room room) {
        String sql = "INSERT INTO rooms (name, roomType, capacity, status) VALUES (?, ?, ?, ?);";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            connectionDB.setAutoCommit(false);

            statement.setString(1, room.getName());
            statement.setString(2, room.getRoomType().name());
            statement.setInt(3, room.getCapacity());
            statement.setString(4, room.getStatus().name());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RuntimeException("Tao room khong thanh cong");
            }

            ResultSet rows = statement.getGeneratedKeys();

            rows.next();
            int roomId = rows.getInt(1);

            seatDao.createSeat(roomId, room.getSeats());

            connectionDB.commit();

            return roomId;
        } catch (SQLException e) {
            try {
                connectionDB.rollback();
            } catch (SQLException ignore) {}
            throw new RuntimeException(e);
        } finally {
            try {
                connectionDB.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public List<Room> findAll() {
        String sql ="SELECT * FROM rooms;";

        try (Statement statement = connectionDB.createStatement()) {

            ResultSet rows = statement.executeQuery(sql);

            return mapResultToObj(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Room> findById(Integer id) {
        String sql = "SELECT * FROM rooms WHERE id = ?;";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet rows = statement.executeQuery();

            List<Room> rooms = mapResultToObj(rows);
            return (rooms.isEmpty()) ? Optional.empty() : Optional.of(rooms.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(Room room) {
        String sql = "UPDATE rooms SET name = ?, room_type = ?, capacity = ? WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setString(1, room.getName());
            statement.setString(2, room.getRoomType().name());
            statement.setInt(3, room.getCapacity());
            statement.setString(4, room.getStatus().name());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("cap nhat room khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStatus(Integer id, RoomStatus status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setString(1, status.name());
            statement.setInt(2, id);

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("cap nhat room status khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Room> mapResultToObj(ResultSet rows) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        while (rows.next()) {
            rooms.add(
                    Room.builder()
                            .id(rows.getInt("id"))
                            .name(rows.getString("name"))
                            .roomType(RoomType.valueOf(rows.getString("room_type")))
                            .capacity(rows.getInt("capacity"))
                            .status(RoomStatus.valueOf(rows.getString("status")))
                            .createdAt(rows.getTimestamp("created_at"))
                            .updatedAt(rows.getTimestamp("updated_at"))
                            .build()
            );
        }

        return rooms;
    }
}
