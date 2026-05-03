package com.application.service.impl;

import com.application.dao.RoomDao;
import com.application.dao.SeatDao;
import com.application.dto.room.CreateRoomDTO;
import com.application.dto.room.UpdateRoomInfoDTO;
import com.application.entity.Room;
import com.application.entity.Seat;
import com.application.entity.enums.RoomStatus;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;
import com.application.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomDao roomDao;

    @Override
    public void createRoom(CreateRoomDTO createRoom) {
        int rowSeat = createRoom.capacity() / createRoom.seatsPerRow();

        List<Seat> seats = new ArrayList<>();

        for (int i = 0; i < rowSeat; i++) {
            for (int j = 0; j < createRoom.seatsPerRow(); j++) {
                seats.add(
                        Seat.builder()
                                .name("%s%2d".formatted(Character.getName(65 + i), j))
                                .status(SeatStatus.AVAILABLE)
                                .type(SeatType.STANDARD)
                                .build()
                );
            }
        }

        int seatPerLastRow = createRoom.capacity() % createRoom.seatsPerRow();

        if (seatPerLastRow != 0) {
            for (int j = 0; j < createRoom.seatsPerRow(); j++) {
                seats.add(
                        Seat.builder()
                                .name("%s%2d".formatted(Character.getName(65 + rowSeat + 1), j))
                                .status(SeatStatus.AVAILABLE)
                                .type(SeatType.STANDARD)
                                .build()
                );
            }
        }

        Integer roomId = roomDao.createRoom(
                Room.builder()
                        .name(createRoom.name())
                        .roomType(createRoom.roomType())
                        .capacity(createRoom.capacity())
                        .seats(seats)
                        .build()
        );
    }

    @Override
    public List<Room> findAll() {
        return roomDao.findAll();
    }

    @Override
    public Room findById(Integer id) {
        return roomDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public void updateInfo(UpdateRoomInfoDTO updateRoomInfo) {
        findById(updateRoomInfo.id());
        roomDao.updateInfo(
                Room.builder()
                        .id(updateRoomInfo.id())
                        .name(updateRoomInfo.name())
                        .roomType(updateRoomInfo.roomType())
                        .capacity(updateRoomInfo.capacity())
                        .build()
        );
    }

    @Override
    public void updateStatus(Integer id, RoomStatus status) {
        findById(id);
        roomDao.updateStatus(id, status);
    }
}
