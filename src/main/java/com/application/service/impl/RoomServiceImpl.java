package com.application.service.impl;

import com.application.dao.RoomDao;
import com.application.entity.Room;
import com.application.service.RoomService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomDao roomDao;

    @Override
    public List<Room> findAll() {
        return roomDao.findAll();
    }
}