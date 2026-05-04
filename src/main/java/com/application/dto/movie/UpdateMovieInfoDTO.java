package com.application.dto.movie;

import java.sql.Timestamp;

public record UpdateMovieInfoDTO(
        Long id,
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
        String ageRating
) {}