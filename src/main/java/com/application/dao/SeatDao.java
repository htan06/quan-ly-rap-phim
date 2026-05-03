package com.application.dao;

import com.application.entity.Room;
import com.application.entity.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SeatDao {
    private Connection connectionDB;

    public SeatDao(Connection connectionDB) {
        this.connectionDB = connectionDB;
    }

    public void createSeat(Integer roomId, List<Seat> seats) {
        String sql = "INSERT INTO seats (name, status, type, room_id) VALUES (?, ?)";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            for (Seat seat : seats) {

                statement.setString(1, seat.getName());
                statement.setString(2, seat.getStatus().name());
                statement.setString(3, seat.getType().name());
                statement.setInt(4, roomId);

                statement.addBatch();
            }

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Tao seat khong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
