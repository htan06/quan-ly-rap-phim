package com.application.dao;

import com.application.entity.Room;
import com.application.entity.Seat;
import com.application.entity.enums.RoomStatus;
import com.application.entity.enums.RoomType;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class RoomDao {

    private final Connection connectionDB;
    private final SeatDao seatDao;

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
        String sql =
                "SELECT r.*, " +
                        "s.id AS seat_id, s.name AS seat_name, s.status AS seat_status, s.type AS seat_type " +
                        "FROM rooms r " +
                        "LEFT JOIN seats s ON r.id = s.room_id";

        try (Statement statement = connectionDB.createStatement()) {
            ResultSet rows = statement.executeQuery(sql);
            return mapResultToObj(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Room> findById(Integer id) {
        String sql =
                "SELECT r.*, " +
                        "s.id AS seat_id, s.name AS seat_name, s.status AS seat_status, s.type AS seat_type " +
                        "FROM rooms r " +
                        "LEFT JOIN seats s ON r.id = s.room_id " +
                        "WHERE r.id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet rows = statement.executeQuery();
            List<Room> rooms = mapResultToObj(rows);

            return rooms.isEmpty()
                    ? Optional.empty()
                    : Optional.of(rooms.getFirst());

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
        Map<Integer, Room> roomMap = new LinkedHashMap<>();

        while (rows.next()) {

            Integer roomId = rows.getInt("id");

            Room room = roomMap.get(roomId);

            if (room == null) {
                room = Room.builder()
                        .id(roomId)
                        .name(rows.getString("name"))
                        .roomType(RoomType.valueOf(rows.getString("room_type")))
                        .capacity(rows.getInt("capacity"))
                        .status(RoomStatus.valueOf(rows.getString("status")))
                        .createdAt(rows.getTimestamp("created_at"))
                        .updatedAt(rows.getTimestamp("updated_at"))
                        .seats(new ArrayList<>())
                        .build();

                roomMap.put(roomId, room);
            }

            // seat mapping
            Long seatId = rows.getLong("seat_id");
            if (!rows.wasNull()) {
                Seat seat = Seat.builder()
                        .id(seatId)
                        .name(rows.getString("seat_name"))
                        .status(SeatStatus.valueOf(rows.getString("seat_status")))
                        .type(SeatType.valueOf(rows.getString("seat_type")))
                        .build();

                room.getSeats().add(seat);
            }
        }

        return new ArrayList<>(roomMap.values());
    }
}
