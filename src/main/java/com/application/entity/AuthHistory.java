package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
public class AuthHistory extends BaseEntity<Long> {
    private String username;
    private Timestamp time;
}
