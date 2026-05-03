package com.application.dto.movie;

import com.application.entity.enums.MovieStatus;

import java.sql.Timestamp;
import java.util.Set;

public record CreateMovieDTO(
        String title,
        String description,
        String director,
        String cast,
        String posterPath,
        Timestamp releaseDate,
        Timestamp endDate,
        String language,
        String subtitleLanguage,
        String country,
        String ageRating,
        MovieStatus status,

        Set<Integer> genresId
) {}