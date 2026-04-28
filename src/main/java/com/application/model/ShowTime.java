package com.application.model;

import java.time.LocalDateTime;

public class ShowTime extends BaseEntity<Long> {

    private Movie movie;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
