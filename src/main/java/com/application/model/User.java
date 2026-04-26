package com.application.model;

import com.application.map.annotation.Column;
import com.application.map.annotation.Entity;
import com.application.model.enums.UserStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class User {
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }

    public User(UUID id, String firstName, String lastName, String email, UserStatus status, String username, String password, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.firstName = firstName;
    }
}
