package com.application.service.impl;

import com.application.dao.SeatDao;
import com.application.entity.Seat;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;
import com.application.service.SeatService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

   private final SeatDao seatDao;

    @Override
    public void updatedStatus(Long id, SeatStatus status) {
        findById(id);
        seatDao.updateStatus(id, status);
    }

    @Override
    public void updatedType(Long id, SeatType type) {
        findById(id);
        seatDao.updatedType(id, type);
    }

    @Override
    public Seat findById(Long id) {
        return seatDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }
}
