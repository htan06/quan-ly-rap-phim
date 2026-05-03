package com.application.entity;

import com.application.entity.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@SuperBuilder
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
