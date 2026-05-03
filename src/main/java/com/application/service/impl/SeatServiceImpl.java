package com.application.service.impl;

import com.application.dao.SeatDao;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;
import com.application.service.SeatService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SeatServiceImpl implements SeatService {

   private final SeatDao seatDao;

    @Override
    public void updatedStatus(Long id, SeatStatus status) {
        seatDao.updateStatus(id, status);
    }

    @Override
    public void updatedType(Long id, SeatType type) {
        seatDao.updatedType(id, type);
    }
}
