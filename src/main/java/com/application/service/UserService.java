package com.application.service;

import com.application.dto.CreateStaffDTO;
import com.application.dto.UpdateStaffInfoDTO;
import com.application.model.User;
import com.application.model.enums.UserStatus;

import java.util.List;

public interface UserService {
    void createStaff(CreateStaffDTO createStaff);

    void changePassword(String username, String newPassword);

    User getUserByUsername(String username);

    void updateStatusUser(String username, UserStatus status);

    void updateUserInfo(UpdateStaffInfoDTO updateStaffInfo);
}
