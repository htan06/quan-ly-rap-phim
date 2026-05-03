package com.application.entity;

import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Seat extends BaseEntity<Long> {

    private Room room;
    private String name;
    private SeatStatus status;
    private SeatType type;
}
