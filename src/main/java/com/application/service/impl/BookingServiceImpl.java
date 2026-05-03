package com.application.service.impl;

import com.application.dao.BookingDao;
import com.application.dao.ShowTimeDao;
import com.application.dto.booking.CreateBookingDTO;
import com.application.entity.*;
import com.application.entity.enums.BookingStatus;
import com.application.service.BookingService;
import com.application.service.ShowTimeService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;
    private final ShowTimeService showTimeService;

    private static final BigDecimal TAX_RATE = new BigDecimal("0.05");

    public record BookingPrice(
            BigDecimal subtotal,
            BigDecimal tax,
            BigDecimal total
    ) {}

    private BookingPrice calculate(ShowTime showTime, int seatCount) {

        BigDecimal pricePerTicket = showTime.getPrice();

        BigDecimal subtotal = pricePerTicket.multiply(BigDecimal.valueOf(seatCount));

        BigDecimal tax = subtotal.multiply(TAX_RATE);

        BigDecimal total = subtotal.add(tax);

        return new BookingPrice(subtotal, tax, total);
    }

    @Override
    public Long create(CreateBookingDTO dto) {

        if (dto.seatIds() == null || dto.seatIds().isEmpty()) {
            throw new RuntimeException("Seat khong duoc de trong");
        }

        ShowTime showTime = showTimeService.findById(dto.showTimeId());

        BookingPrice price = calculate(showTime, dto.seatIds().size());

        Booking booking = Booking.builder()
                .showTime(showTime)
                .staff(User.builder().id(dto.staffId()).build())
                .status(BookingStatus.PENDING)
                .totalPrice(price.total())
                .totalTax(price.tax())
                .bookingDetails(
                        (List<BookingDetail>) dto.seatIds().stream()
                                .map(seatId -> BookingDetail.builder()
                                        .seat(Seat.builder().id(seatId).build())
                                        .build())
                                .toList()
                ).build();

        return bookingDao.create(booking);
    }

    @Override
    public List<Long> seatBookedId(Long showTimeId) {
        return bookingDao.findBookedSeatIdsByShowTime(showTimeId);
    }

    @Override
    public Booking findFullInfoById(Long id) {
        return bookingDao.findFullInfoById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
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
