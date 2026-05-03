package com.application.dto.auth;

public record UserLoginRequest (
        String username,
        String password
){}
