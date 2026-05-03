package com.application.service.impl;

import com.application.dao.MovieDao;
import com.application.dto.movie.CreateMovieDTO;
import com.application.dto.movie.UpdateMovieInfoDTO;
import com.application.entity.Genre;
import com.application.entity.Movie;
import com.application.entity.enums.MovieStatus;
import com.application.service.MovieService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
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
                        .posterPath(createMovie.posterPath())
                        .releaseDate(createMovie.releaseDate())
                        .endDate(createMovie.endDate())
                        .language(createMovie.language())
                        .subtitleLanguage(createMovie.subtitleLanguage())
                        .country(createMovie.country())
                        .ageRating(createMovie.ageRating())
                        .status(createMovie.status())
                        .genres(createMovie.genresId().stream().map(gid ->
                                Genre.builder()
                                        .id(gid)
                                        .build()
                        ).collect(Collectors.toSet()))
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
        movieDao.updateInfo(
                Movie.builder()
                        .title(updateMovieInfo.title())
                        .description(updateMovieInfo.description())
                        .director(updateMovieInfo.director())
                        .cast(updateMovieInfo.cast())
                        .posterPath(updateMovieInfo.posterPath())
                        .releaseDate(updateMovieInfo.releaseDate())
                        .endDate(updateMovieInfo.endDate())
                        .language(updateMovieInfo.language())
                        .subtitleLanguage(updateMovieInfo.subtitleLanguage())
                        .country(updateMovieInfo.country())
                        .ageRating(updateMovieInfo.ageRating())
                        .genres(updateMovieInfo.genresId().stream().map(gid ->
                                Genre.builder()
                                        .id(gid)
                                        .build()
                        ).collect(Collectors.toSet()))
                        .build()
        );
    }

    @Override
    public void updateStatus(Long id, MovieStatus status) {
        movieDao.updateStatus(id, status);
    }
}
