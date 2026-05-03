package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
public class Genre extends BaseEntity<Integer> {

    private String name;

    private Set<Movie> movies;
}
