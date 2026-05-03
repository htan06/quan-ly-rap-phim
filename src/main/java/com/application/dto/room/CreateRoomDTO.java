package com.application.dto.room;

import com.application.entity.enums.RoomType;

public record CreateRoomDTO (
    String name,
    RoomType roomType,
    int capacity,
    int seatsPerRow
) {}
