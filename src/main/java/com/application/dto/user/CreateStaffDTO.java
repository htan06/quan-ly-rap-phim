package com.application.dto.user;

public record CreateStaffDTO(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String username,
        String password
){}
