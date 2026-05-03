package com.application.dto.showtime;

import java.sql.Timestamp;

public record UpdateShowTimeDTO(
        Long id,
        Long movieId,
        Integer roomId,
        Timestamp startTime,
        Timestamp endTime
) {}
