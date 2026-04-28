package com.application.model;

import com.application.map.annotation.Column;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity<T> {
    @Column(name = "id")
    protected T id;

    @Column(name = "created_at")
    protected Timestamp createdAt;

    @Column(name = "updated_at")
    protected Timestamp updatedAt;
}
