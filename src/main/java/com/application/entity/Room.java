package com.application.entity;

import com.application.entity.enums.RoomStatus;
import com.application.entity.enums.RoomType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class Room extends BaseEntity<Integer> {

    private String name;
    private RoomType roomType;
    private int capacity;
    private RoomStatus status;

    private List<Seat> seats;
}
