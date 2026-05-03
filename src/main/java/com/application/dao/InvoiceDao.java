package com.application.dao;

import com.application.entity.Invoice;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InvoiceDao {

    private final Connection connectionDb;

    public Optional<Invoice> findByBookingId(Long bookingId) {

        String sql =
                "SELECT " +
                        "b.id as booking_id, " +
                        "b.total_tax, b.total_price, " +
                        "st.start_time, st.end_time, " +
                        "m.title as movie_title, " +
                        "r.name as room_name, " +
                        "u.first_name, u.last_name, " +
                        "s.name as seat_name " +
                        "FROM bookings b " +
                        "JOIN show_times st ON b.show_time_id = st.id " +
                        "JOIN movies m ON st.movie_id = m.id " +
                        "JOIN rooms r ON st.room_id = r.id " +
                        "JOIN users u ON b.staff_id = u.id " +
                        "JOIN booking_details bd ON bd.booking_id = b.id " +
                        "JOIN seats s ON bd.seat_id = s.id " +
                        "WHERE b.id = ?";

        try (PreparedStatement stmt = connectionDb.prepareStatement(sql)) {

            stmt.setLong(1, bookingId);

            ResultSet rs = stmt.executeQuery();

            Invoice invoice = null;
            List<String> seats = new ArrayList<>();

            while (rs.next()) {

                if (invoice == null) {

                    invoice = Invoice.builder()
                            .bookingId(rs.getLong("booking_id"))
                            .movieTitle(rs.getString("movie_title"))
                            .roomName(rs.getString("room_name"))
                            .startTime(rs.getTimestamp("start_time"))
                            .endTime(rs.getTimestamp("end_time"))
                            .totalTax(rs.getBigDecimal("total_tax"))
                            .totalPrice(rs.getBigDecimal("total_price"))
                            .staffName(
                                    rs.getString("first_name") + " " +
                                            rs.getString("last_name")
                            )
                            .build();
                }

                seats.add(rs.getString("seat_name"));
            }

            if (invoice == null) return Optional.empty();

            invoice.setSeats(seats);

            return Optional.of(invoice);

        } catch (SQLException e) {
            throw new RuntimeException("Fetch invoice failed", e);
        }
    }
}
