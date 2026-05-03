package com.application.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@Builder
public class Invoice {

    private Long bookingId;

    private String movieTitle;
    private String roomName;

    private Timestamp startTime;
    private Timestamp endTime;

    private List<String> seats;

    private BigDecimal totalTax;
    private BigDecimal totalPrice;

    private String staffName;
}
