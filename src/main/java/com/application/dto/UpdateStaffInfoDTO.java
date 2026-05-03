package com.application.dto;

public record UpdateStaffInfoDTO(
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
){}
