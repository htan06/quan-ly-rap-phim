package com.application.entity;

import com.application.entity.enums.MovieStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class Movie extends BaseEntity<Long> {

    private String title;
    private String description;
    private String director;
    private String cast;
    private String genre;
    private Integer duration;
    private Timestamp releaseDate;
    private Timestamp endDate;
    private String language;
    private String subtitleLanguage;
    private String country;
    private String ageRating;
    private MovieStatus status;
}
