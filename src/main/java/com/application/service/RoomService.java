package com.application.service;

import com.application.dto.room.CreateRoomDTO;
import com.application.dto.room.UpdateRoomInfoDTO;
import com.application.entity.Room;
import com.application.entity.enums.RoomStatus;

import java.util.List;

public interface RoomService {
    void createRoom(CreateRoomDTO createRoom);
    List<Room> getRooms();
    Room getRoom(Integer id);
    void updateInfo(UpdateRoomInfoDTO updateRoomInfo);
    void updateStatus(Integer id, RoomStatus status);
}
