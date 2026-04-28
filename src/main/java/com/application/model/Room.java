package com.application.model;

import com.application.model.enums.RoomStatus;
import com.application.model.enums.RoomType;

import java.util.List;

public class Room extends BaseEntity<Integer> {

    private String name;
    private RoomType roomType;
    private int capacity;
    private RoomStatus status;

    private List<Seat> seats;
}
