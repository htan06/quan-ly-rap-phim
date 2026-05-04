package com.application.service;

import com.application.dto.auth.UserLoginRequest;
import com.application.dto.user.CreateStaffDTO;

public interface AuthenticationService {
    void createStaff(CreateStaffDTO createStaff);

    void changePassword(String username, String newPassword);

    boolean login(UserLoginRequest userLogin);

    void logout();
}