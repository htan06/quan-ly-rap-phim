package com.application.service;

import com.application.entity.Seat;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;

import java.util.List;

public interface SeatService {
    Seat findById(Long id);
    List<Seat> findByRoomId(Integer id);
}
