package com.application.dto.room;

import com.application.entity.enums.RoomType;

public record UpdateRoomInfoDTO (
    Integer id,
    String name,
    RoomType roomType,
    int capacity
) {}
