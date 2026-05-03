package com.application.dao;

import com.application.entity.Movie;
import com.application.entity.Room;
import com.application.entity.ShowTime;
import lombok.AllArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ShowTimeDao {

    private final Connection connectionDB;

    public Long createShowTime(ShowTime showTime) {
        String sql = "INSERT INTO show_times (" +
                "movie_id, room_id, start_time, end_time" +
                ") VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, showTime.getMovie().getId());
            statement.setInt(2, showTime.getRoom().getId());
            statement.setTimestamp(3, showTime.getStartTime());
            statement.setTimestamp(4, showTime.getEndTime());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Tao showtime khong thanh cong");
            }

            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new RuntimeException("Tao showtime khong thanh cong", e);
        }
    }

    public List<ShowTime> findAll() {
        String sql = "SELECT * FROM show_times";

        try (Statement statement = connectionDB.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            return mapResultToObj(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ShowTime> findById(Long id) {
        String sql = "SELECT * FROM show_times WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            List<ShowTime> list = mapResultToObj(rs);

            return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ShowTime> findByMovieId(Long movieId) {
        String sql = "SELECT * FROM show_times WHERE movie_id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {
            statement.setLong(1, movieId);

            ResultSet rs = statement.executeQuery();
            return mapResultToObj(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(ShowTime showTime) {
        String sql = "UPDATE show_times SET " +
                "movie_id = ?, " +
                "room_id = ?, " +
                "start_time = ?, " +
                "end_time = ?, " +
                "updated_at = GETDATE() " +
                "WHERE id = ?";

        try (PreparedStatement statement = connectionDB.prepareStatement(sql)) {

            statement.setLong(1, showTime.getMovie().getId());
            statement.setInt(2, showTime.getRoom().getId());
            statement.setTimestamp(3, showTime.getStartTime());
            statement.setTimestamp(4, showTime.getEndTime());
            statement.setLong(5, showTime.getId());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Cap nhat showtime khong thanh cong");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ShowTime> mapResultToObj(ResultSet rs) throws SQLException {
        List<ShowTime> list = new ArrayList<>();

        while (rs.next()) {
            list.add(
                    ShowTime.builder()
                            .id(rs.getLong("id"))
                            .movie(Movie.builder()
                                    .id(rs.getLong("movie_id"))
                                    .build())
                            .room(Room.builder()
                                    .id(rs.getInt("room_id"))
                                    .build())
                            .startTime(rs.getTimestamp("start_time"))
                            .endTime(rs.getTimestamp("end_time"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build()
            );
        }

        return list;
    }
}
