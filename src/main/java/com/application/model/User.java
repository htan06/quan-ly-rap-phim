package com.application.model;

import com.application.map.annotation.Column;
import com.application.map.annotation.Entity;
import com.application.model.enums.UserStatus;
import java.util.UUID;

import java.util.Set;

@Entity
public class User extends BaseEntity<UUID> {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private UserStatus status;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    private Set<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", created_at=" + createdAt +
                ", updated_at=" + updatedAt +
                '}';
    }
}
