package com.application.entity;

import com.application.entity.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
public class Payment extends BaseEntity<Long> {

    private Booking booking;

    private BigDecimal amount;
    private BigDecimal receivedAmount;
    private BigDecimal changeAmount;

    private PaymentStatus paymentStatus;

}
