package com.application.dao;

import com.application.entity.*;
import com.application.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookingDao {

    private final Connection connectionDb;

    public Long create(Booking booking) {

        String insertBooking = "INSERT INTO bookings (" +
                "show_time_id, membership_id, staff_id, status, total_tax, total_price" +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        String insertDetail = "INSERT INTO booking_details (booking_id, seat_id) VALUES (?, ?)";

        try (
                PreparedStatement bookingStmt = connectionDb.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement detailStmt = connectionDb.prepareStatement(insertDetail)
        ) {

            connectionDb.setAutoCommit(false);

            // insert booking
            bookingStmt.setLong(1, booking.getShowTime().getId());

            if (booking.getMembership() != null) {
                bookingStmt.setLong(2, booking.getMembership().getId());
            } else {
                bookingStmt.setNull(2, Types.BIGINT);
            }

            bookingStmt.setLong(3, booking.getStaff().getId());
            bookingStmt.setString(4, booking.getStatus().name());
            bookingStmt.setBigDecimal(5, booking.getTotalTax() != null ? booking.getTotalTax() : BigDecimal.ZERO);
            bookingStmt.setBigDecimal(6, booking.getTotalPrice() != null ? booking.getTotalPrice() : BigDecimal.ZERO);

            int rows = bookingStmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Tao booking that bai");
            }

            ResultSet rs = bookingStmt.getGeneratedKeys();
            rs.next();
            long bookingId = rs.getLong(1);

            // insert booking_details
            for (BookingDetail detail : booking.getBookingDetails()) {
                detailStmt.setLong(1, bookingId);
                detailStmt.setLong(2, detail.getSeat().getId());
                detailStmt.addBatch();
            }

            int[] batchResult = detailStmt.executeBatch();

            if (batchResult.length == 0) {
                throw new RuntimeException("Tao booking detail that bai");
            }

            connectionDb.commit();
            return bookingId;

        } catch (SQLException e) {
            try {
                connectionDb.rollback();
            } catch (SQLException ignore) {}
            throw new RuntimeException("Tao booking that bai", e);
        } finally {
            try {
                connectionDb.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }

    public List<Long> findBookedSeatIdsByShowTime(Long showTimeId) {
        String sql =
                "SELECT bd.seat_id " +
                        "FROM bookings b " +
                        "JOIN booking_details bd ON b.id = bd.booking_id " +
                        "WHERE b.show_time_id = ? " +
                        "AND b.status IN ('PENDING', 'PAID')";

        try (PreparedStatement ps = connectionDb.prepareStatement(sql)) {

            ps.setLong(1, showTimeId);

            ResultSet rs = ps.executeQuery();

            List<Long> seatIds = new ArrayList<>();

            while (rs.next()) {
                seatIds.add(rs.getLong("seat_id"));
            }

            return seatIds;

        } catch (SQLException e) {
            throw new RuntimeException("Error get booked seats", e);
        }
    }

    public List<Booking> findAll() {
        String sql = "SELECT * FROM bookings";

        try (Statement stmt = connectionDb.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            return mapBooking(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Booking> findById(Long id) {
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (PreparedStatement stmt = connectionDb.prepareStatement(sql)) {
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            List<Booking> list = mapBooking(rs);

            return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Booking> findFullInfoById(Long bookingId) {

        String sql =
                "SELECT " +
                        "b.id as booking_id, b.status, b.total_tax, b.total_price, b.created_at, b.updated_at, " +
                        "st.id as showtime_id, st.start_time, st.end_time, " +
                        "m.id as movie_id, m.title as movie_title, " +
                        "r.id as room_id, r.name as room_name, " +
                        "s.id as seat_id, s.name as seat_name " +
                        "FROM bookings b " +
                        "JOIN show_times st ON b.show_time_id = st.id " +
                        "JOIN movies m ON st.movie_id = m.id " +
                        "JOIN rooms r ON st.room_id = r.id " +
                        "JOIN booking_details bd ON bd.booking_id = b.id " +
                        "JOIN seats s ON bd.seat_id = s.id " +
                        "WHERE b.id = ?";

        try (PreparedStatement stmt = connectionDb.prepareStatement(sql)) {

            stmt.setLong(1, bookingId);

            ResultSet rs = stmt.executeQuery();

            Booking booking = null;
            ShowTime showTime = null;
            Movie movie = null;
            Room room = null;

            List<BookingDetail> details = new ArrayList<>();

            while (rs.next()) {

                // init 1 lần
                if (booking == null) {

                    movie = Movie.builder()
                            .id(rs.getLong("movie_id"))
                            .title(rs.getString("movie_title"))
                            .build();

                    room = Room.builder()
                            .id(rs.getInt("room_id"))
                            .name(rs.getString("room_name"))
                            .build();

                    showTime = ShowTime.builder()
                            .id(rs.getLong("showtime_id"))
                            .movie(movie)
                            .room(room)
                            .startTime(rs.getTimestamp("start_time"))
                            .endTime(rs.getTimestamp("end_time"))
                            .build();

                    booking = Booking.builder()
                            .id(rs.getLong("booking_id"))
                            .showTime(showTime)
                            .status(BookingStatus.valueOf(rs.getString("status")))
                            .totalTax(rs.getBigDecimal("total_tax"))
                            .totalPrice(rs.getBigDecimal("total_price"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }

                // mỗi row là 1 seat
                Seat seat = Seat.builder()
                        .id(rs.getLong("seat_id"))
                        .name(rs.getString("seat_name"))
                        .build();

                BookingDetail detail = BookingDetail.builder()
                        .seat(seat)
                        .build();

                details.add(detail);
            }

            if (booking == null) return Optional.empty();

            booking.setBookingDetails(details);

            return Optional.of(booking);

        } catch (SQLException e) {
            throw new RuntimeException("Error fetch full booking info", e);
        }
    }

    public void updateStatus(Long id, BookingStatus status) {
        String sql = "UPDATE bookings SET status = ?, updated_at = GETDATE() WHERE id = ?";

        try (PreparedStatement stmt = connectionDb.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, id);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Update status that bai");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Booking> mapBooking(ResultSet rs) throws SQLException {
        List<Booking> list = new ArrayList<>();

        while (rs.next()) {
            list.add(
                    Booking.builder()
                            .id(rs.getLong("id"))
                            .showTime(ShowTime.builder()
                                    .id(rs.getLong("show_time_id"))
                                    .build())
                            .membership(
                                    rs.getObject("membership_id") != null
                                            ? Membership.builder().id(rs.getLong("membership_id")).build()
                                            : null
                            )
                            .staff(User.builder()
                                    .id(rs.getLong("staff_id"))
                                    .build())
                            .status(BookingStatus.valueOf(rs.getString("status")))
                            .qrCode(rs.getBytes("qr_code"))
                            .totalTax(rs.getBigDecimal("total_tax"))
                            .totalPrice(rs.getBigDecimal("total_price"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build()
            );
        }

        return list;
    }
}
