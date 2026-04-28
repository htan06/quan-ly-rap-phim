package com.application.model;

import com.application.model.enums.BookingStatus;

import java.math.BigDecimal;
import java.util.List;

public class Booking extends BaseEntity<Long> {

    private ShowTime showTime;
    private Membership membership;
    private User staff;

    private byte[] qrCode;
    private BookingStatus status;

    private BigDecimal totalTax;
    private BigDecimal totalPrice;

    private List<BookingDetail> bookingDetails;

}
