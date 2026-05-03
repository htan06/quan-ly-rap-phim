package com.application.dto.movie;

import com.application.entity.enums.MovieStatus;

import java.sql.Timestamp;
import java.util.Set;

public record UpdateMovieInfoDTO(
        Long id,
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

        Set<Integer> genresId
) {}