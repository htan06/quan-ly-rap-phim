package com.application.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
public abstract class BaseEntity<T> {
    protected T id;

    protected Timestamp createdAt;

    protected Timestamp updatedAt;
}
