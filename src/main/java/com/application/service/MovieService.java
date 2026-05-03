package com.application.service;

import com.application.dto.movie.CreateMovieDTO;
import com.application.dto.movie.UpdateMovieInfoDTO;
import com.application.entity.Movie;
import com.application.entity.enums.MovieStatus;

import java.util.List;

public interface MovieService {
    Long createMovie(CreateMovieDTO createMovie);

    List<Movie> findAll();

    Movie findById(Long id);

    List<Movie> findByStatus(MovieStatus status);

    void updateInfo(UpdateMovieInfoDTO updateMovieInfo);

    void updateStatus(Long id, MovieStatus status);
}
