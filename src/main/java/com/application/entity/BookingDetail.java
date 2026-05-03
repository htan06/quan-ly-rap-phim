package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BookingDetail extends BaseEntity<Long> {

    private Booking booking;
    private Seat seat;
}
