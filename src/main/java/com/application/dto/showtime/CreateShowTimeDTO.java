package com.application.dto.showtime;

import java.sql.Timestamp;

public record CreateShowTimeDTO(
        Long movieId,
        Integer roomId,
        Timestamp startTime,
        Timestamp endTime
) {}
