package com.application.service.impl;

import com.application.dao.SeatDao;
import com.application.entity.Seat;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;
import com.application.service.SeatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

   private final SeatDao seatDao;

    @Override
    public Seat findById(Long id) {
        return seatDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    @Override
    public List<Seat> findByRoomId(Integer id) {
        return seatDao.findByRoomId(id);
    }
}
