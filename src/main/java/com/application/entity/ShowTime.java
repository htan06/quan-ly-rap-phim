package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
public class ShowTime extends BaseEntity<Long> {

    private Movie movie;
    private Room room;
    private BigDecimal price;
    private Timestamp startTime;
    private Timestamp endTime;
}
