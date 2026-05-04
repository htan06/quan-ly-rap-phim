package com.application.service;

import com.application.dto.showtime.CreateShowTimeDTO;
import com.application.dto.showtime.UpdateShowTimeDTO;
import com.application.entity.ShowTime;

import java.time.LocalDate;
import java.util.List;

public interface ShowTimeService {
    Long createShowTime(CreateShowTimeDTO createShowTimeDTO);

    List<ShowTime> findByDate(LocalDate date);

    List<ShowTime> findAll();

    ShowTime findById(Long id);

    List<ShowTime> findByMovie(Long movieId);

    void updateInfo(UpdateShowTimeDTO updateShowTimeDTO);

}
