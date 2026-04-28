package com.application.model;

import com.application.model.enums.SeatStatus;
import com.application.model.enums.SeatType;

public class Seat extends BaseEntity<Long> {

    private Room room;
    private String name;
    private SeatStatus status;
    private SeatType type;
}
