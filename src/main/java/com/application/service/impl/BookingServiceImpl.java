package com.application.service.impl;

import com.application.dao.BookingDao;
import com.application.dto.booking.CreateBookingDTO;
import com.application.entity.*;
import com.application.entity.enums.BookingStatus;
import com.application.service.BookingService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;

    @Override
    public Long create(CreateBookingDTO dto) {

        if (dto.seatIds() == null || dto.seatIds().isEmpty()) {
            throw new RuntimeException("Seat khong duoc de trong");
        }

        return bookingDao.create(
                Booking.builder()
                        .showTime(ShowTime.builder()
                                .id(dto.showTimeId())
                                .build())
                        .membership(dto.membershipId() != null
                                ? Membership.builder().id(dto.membershipId()).build()
                                : null)
                        .staff(User.builder()
                                .id(dto.staffId())
                                .build())
                        .status(BookingStatus.PENDING)
                        .bookingDetails(
                                (List<BookingDetail>) dto.seatIds().stream()
                                        .map(seatId -> BookingDetail.builder()
                                                .seat(Seat.builder()
                                                        .id(seatId)
                                                        .build())
                                                .build())
                                        .toList()
                        )
                        .build()
        );
    }

    @Override
    public void updateStatus(Long id, BookingStatus status) {
        findById(id);
        bookingDao.updateStatus(id, status);
    }

    @Override
    public Booking findById(Long id) {
        return bookingDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<Booking> findAll() {
        return bookingDao.findAll();
    }
}
