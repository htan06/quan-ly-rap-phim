package com.application.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Session {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
}
