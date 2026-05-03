package com.application.dao;

import com.application.entity.*;
import com.application.entity.enums.*;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
public class ShowTimeDao {

    private final Connection connectionDB;

    public Long createShowTime(ShowTime showTime) {
        String sql = "INSERT INTO show_times (movie_id, room_id, price, start_time, end_time) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connectionDB.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, showTime.getMovie().getId());
            ps.setInt(2, showTime.getRoom().getId());
            ps.setBigDecimal(3, showTime.getPrice());
            ps.setTimestamp(4, showTime.getStartTime());
            ps.setTimestamp(5, showTime.getEndTime());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new RuntimeException("Tao showtime khong thanh cong");

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Tao showtime khong thanh cong", e);
        }
    }

    public List<ShowTime> findAll() {
        // Không JOIN seats ở đây để tránh data lớn — chỉ lấy room cơ bản
        String sql = """
                SELECT st.id, st.movie_id, st.price, st.start_time, st.end_time,
                       st.created_at, st.updated_at,
                       r.id   AS room_id,   r.name      AS room_name,
                       r.room_type,         r.capacity,  r.status AS room_status
                FROM show_times st
                JOIN rooms r ON st.room_id = r.id
                """;

        try (Statement s = connectionDB.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            return mapRows(rs, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ShowTime> findById(Long id) {
        // JOIN seats vì SoDoGhe cần danh sách ghế
        String sql = """
                SELECT st.id, st.movie_id, st.price, st.start_time, st.end_time,
                       st.created_at, st.updated_at,
                       r.id   AS room_id,   r.name      AS room_name,
                       r.room_type,         r.capacity,  r.status AS room_status,
                       se.id  AS seat_id,   se.name     AS seat_name,
                       se.status AS seat_status,         se.type  AS seat_type
                FROM show_times st
                JOIN rooms r  ON st.room_id = r.id
                LEFT JOIN seats se ON se.room_id = r.id
                WHERE st.id = ?
                """;

        try (PreparedStatement ps = connectionDB.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            List<ShowTime> list = mapRows(rs, true);
            return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ShowTime> findByMovieId(Long movieId) {
        String sql = """
                SELECT st.id, st.movie_id, st.price, st.start_time, st.end_time,
                       st.created_at, st.updated_at,
                       r.id   AS room_id,   r.name      AS room_name,
                       r.room_type,         r.capacity,  r.status AS room_status
                FROM show_times st
                JOIN rooms r ON st.room_id = r.id
                WHERE st.movie_id = ?
                """;

        try (PreparedStatement ps = connectionDB.prepareStatement(sql)) {
            ps.setLong(1, movieId);
            ResultSet rs = ps.executeQuery();
            return mapRows(rs, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateInfo(ShowTime showTime) {
        String sql = """
                UPDATE show_times SET
                    movie_id   = ?,
                    room_id    = ?,
                    price      = ?,
                    start_time = ?,
                    end_time   = ?,
                    updated_at = GETDATE()
                WHERE id = ?
                """;

        try (PreparedStatement ps = connectionDB.prepareStatement(sql)) {
            ps.setLong(1, showTime.getMovie().getId());
            ps.setInt(2, showTime.getRoom().getId());
            ps.setBigDecimal(3, showTime.getPrice());
            ps.setTimestamp(4, showTime.getStartTime());
            ps.setTimestamp(5, showTime.getEndTime());
            ps.setLong(6, showTime.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) throw new RuntimeException("Cap nhat showtime khong thanh cong");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ShowTime> mapRows(ResultSet rs, boolean withSeats) throws SQLException {
        // Dùng LinkedHashMap để giữ thứ tự và gom nhiều row cùng showtime_id
        Map<Long, ShowTime> map = new LinkedHashMap<>();

        while (rs.next()) {
            long stId = rs.getLong("id");

            if (!map.containsKey(stId)) {
                Room room = Room.builder()
                        .id(rs.getInt("room_id"))
                        .name(rs.getString("room_name"))
                        .roomType(RoomType.valueOf(rs.getString("room_type")))
                        .capacity(rs.getInt("capacity"))
                        .status(RoomStatus.valueOf(rs.getString("room_status")))
                        .seats(withSeats ? new ArrayList<>() : null)
                        .build();

                ShowTime st = ShowTime.builder()
                        .id(stId)
                        .movie(Movie.builder().id(rs.getLong("movie_id")).build())
                        .room(room)
                        .price(rs.getBigDecimal("price"))
                        .startTime(rs.getTimestamp("start_time"))
                        .endTime(rs.getTimestamp("end_time"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .updatedAt(rs.getTimestamp("updated_at"))
                        .build();

                map.put(stId, st);
            }

            // Gom ghế nếu có JOIN seats
            if (withSeats) {
                long seatId = rs.getLong("seat_id");
                if (seatId != 0) {          // LEFT JOIN → có thể null
                    Seat seat = Seat.builder()
                            .id(seatId)
                            .name(rs.getString("seat_name"))
                            .status(SeatStatus.valueOf(rs.getString("seat_status")))
                            .type(SeatType.valueOf(rs.getString("seat_type")))
                            .build();
                    map.get(stId).getRoom().getSeats().add(seat);
                }
            }
        }

        return new ArrayList<>(map.values());
    }
}