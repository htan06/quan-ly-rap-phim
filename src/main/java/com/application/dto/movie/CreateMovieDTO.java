package com.application.dto.movie;

import com.application.entity.enums.MovieStatus;

import java.sql.Timestamp;

public record CreateMovieDTO(
        String title,
        String description,
        String director,
        String cast,
        String genre,
        Integer duration,
        Timestamp releaseDate,
        Timestamp endDate,
        String language,
        String subtitleLanguage,
        String country,
        String ageRating,
        MovieStatus status
) {}