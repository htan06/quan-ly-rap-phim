package com.application.model;

import java.util.Set;

public class Genre extends BaseEntity<Integer> {

    private String name;

    private Set<Movie> movies;
}
