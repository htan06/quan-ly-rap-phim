package com.application.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public abstract class BaseEntity<T> {
    protected T id;

    protected Timestamp createdAt;

    protected Timestamp updatedAt;
}
