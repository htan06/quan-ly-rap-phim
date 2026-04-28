package com.application.model;

import com.application.model.enums.PaymentStatus;

import java.math.BigDecimal;

public class Payment extends BaseEntity<Long> {

    private Booking booking;

    private BigDecimal amount;
    private BigDecimal receivedAmount;
    private BigDecimal changeAmount;

    private PaymentStatus paymentStatus;

}
