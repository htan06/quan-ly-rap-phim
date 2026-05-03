package com.application.service;

import com.application.dto.booking.CreateBookingDTO;
import com.application.entity.Booking;
import com.application.entity.enums.BookingStatus;

import java.util.List;

public interface BookingService {

    Long create(CreateBookingDTO dto);

    void updateStatus(Long id, BookingStatus status);

    Booking findById(Long id);

    Booking findFullInfoById(Long id);

    List<Booking> findAll();

    List<Long> seatBookedId(Long showTimeId);
}
