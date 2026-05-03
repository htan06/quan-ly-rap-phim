package com.application.dto.user;

public record UpdateStaffInfoDTO(
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
){}
