package com.application.service;

import com.application.dto.room.CreateRoomDTO;
import com.application.dto.room.UpdateRoomInfoDTO;
import com.application.entity.Room;
import com.application.entity.enums.RoomStatus;

import java.util.List;

public interface RoomService {
    List<Room> findAll();
}
