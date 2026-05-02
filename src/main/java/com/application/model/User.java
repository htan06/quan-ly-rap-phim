package com.application.model;

import com.application.model.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import java.util.Set;

@Setter
@Getter
@Builder
public class User extends BaseEntity<Long> {
    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private UserStatus status;

    private String username;

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
