package com.application.service.impl;

import com.application.dao.ShowTimeDao;
import com.application.dto.showtime.CreateShowTimeDTO;
import com.application.dto.showtime.UpdateShowTimeDTO;
import com.application.entity.Movie;
import com.application.entity.Room;
import com.application.entity.ShowTime;
import com.application.service.ShowTimeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeDao showTimeDao;

    @Override
    public Long createShowTime(CreateShowTimeDTO createShowTimeDTO) {
        return showTimeDao.createShowTime(
                ShowTime.builder()
                        .movie(Movie.builder()
                                .id(createShowTimeDTO.movieId())
                                .build())
                        .room(Room.builder()
                                .id(createShowTimeDTO.roomId())
                                .build())
                        .startTime(createShowTimeDTO.startTime())
                        .endTime(createShowTimeDTO.endTime())
                        .build()
        );
    }

    @Override
    public List<ShowTime> findAll() {
        return showTimeDao.findAll();
    }

    @Override
    public ShowTime findById(Long id) {
        return showTimeDao.findById(id)
                .orElseThrow(() -> new RuntimeException("ShowTime not found"));
    }

    @Override
    public List<ShowTime> findByMovie(Long movieId) {
        return showTimeDao.findByMovieId(movieId);
    }

    @Override
    public void updateInfo(UpdateShowTimeDTO updateShowTimeDTO) {
        showTimeDao.updateInfo(
                ShowTime.builder()
                        .id(updateShowTimeDTO.id())
                        .movie(Movie.builder()
                                .id(updateShowTimeDTO.movieId())
                                .build())
                        .room(Room.builder()
                                .id(updateShowTimeDTO.roomId())
                                .build())
                        .startTime(updateShowTimeDTO.startTime())
                        .endTime(updateShowTimeDTO.endTime())
                        .build()
        );
    }
}
