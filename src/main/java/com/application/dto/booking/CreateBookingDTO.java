package com.application.dto.booking;

import java.util.List;

public record CreateBookingDTO(
        Long showTimeId,
        Long membershipId,   // có thể null
        Long staffId,
        List<Long> seatIds
) {}
