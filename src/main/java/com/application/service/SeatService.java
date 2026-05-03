package com.application.service;

import com.application.entity.Seat;
import com.application.entity.enums.SeatStatus;
import com.application.entity.enums.SeatType;

public interface SeatService {
    void updatedStatus(Long id, SeatStatus status);
    void updatedType(Long id, SeatType type);
    Seat findById(Long id);
}
