package com.application.dto.showtime;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CreateShowTimeDTO(
        Long movieId,
        Integer roomId,
        BigDecimal price,
        Timestamp startTime,
        Timestamp endTime
) {}
