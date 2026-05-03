package com.application.service;

import com.application.dto.UserLoginRequest;
import com.application.dto.CreateStaffDTO;

public interface AuthenticationService {
    void createStaff(CreateStaffDTO createStaff);

    void changePassword(String username, String newPassword);

    void setRoleUser(Long userId, Integer roleId);

    boolean login(UserLoginRequest userLogin);

    void logout();
}