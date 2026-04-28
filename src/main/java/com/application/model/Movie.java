package com.application.model;

import com.application.model.enums.MovieStatus;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class Movie extends BaseEntity<UUID> {

    private String title;
    private String description;
    private String director;
    private String cast;
    private String posterPath;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private String language;
    private String subtitleLanguage;
    private String country;
    private String ageRating;
    private MovieStatus status;

    private Set<Genre> genres;

}
