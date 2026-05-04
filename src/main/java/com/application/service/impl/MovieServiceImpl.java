package com.application.service.impl;

import com.application.dao.MovieDao;
import com.application.dto.movie.CreateMovieDTO;
import com.application.dto.movie.UpdateMovieInfoDTO;
import com.application.entity.Movie;
import com.application.entity.enums.MovieStatus;
import com.application.service.MovieService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieDao movieDao;

    @Override
    public Long createMovie(CreateMovieDTO createMovie) {
        return movieDao.createMovie(
                Movie.builder()
                        .title(createMovie.title())
                        .description(createMovie.description())
                        .director(createMovie.director())
                        .cast(createMovie.cast())
                        .genre(createMovie.genre())
                        .duration(createMovie.duration())
                        .releaseDate(createMovie.releaseDate())
                        .endDate(createMovie.endDate())
                        .language(createMovie.language())
                        .subtitleLanguage(createMovie.subtitleLanguage())
                        .country(createMovie.country())
                        .ageRating(createMovie.ageRating())
                        .status(createMovie.status())
                        .build()
        );
    }

    @Override
    public List<Movie> findAll() {
        return movieDao.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    @Override
    public List<Movie> findByStatus(MovieStatus status) {
        return movieDao.findAllByStatus(status);
    }

    @Override
    public void updateInfo(UpdateMovieInfoDTO updateMovieInfo) {
        findById(updateMovieInfo.id());
        movieDao.updateInfo(
                Movie.builder()
                        .id(updateMovieInfo.id())
                        .title(updateMovieInfo.title())
                        .description(updateMovieInfo.description())
                        .director(updateMovieInfo.director())
                        .cast(updateMovieInfo.cast())
                        .genre(updateMovieInfo.genre())
                        .duration(updateMovieInfo.duration())
                        .releaseDate(updateMovieInfo.releaseDate())
                        .endDate(updateMovieInfo.endDate())
                        .language(updateMovieInfo.language())
                        .subtitleLanguage(updateMovieInfo.subtitleLanguage())
                        .country(updateMovieInfo.country())
                        .ageRating(updateMovieInfo.ageRating())
                        .build()
        );
    }

    @Override
    public void updateStatus(Long id, MovieStatus status) {
        findById(id);
        movieDao.updateStatus(id, status);
    }
}
