package com.application.service;

import com.application.dto.UpdateStaffInfoDTO;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;

public interface UserService {

    User getUserByUsername(String username);

    void updateStatusUser(String username, UserStatus status);

    void updateUserInfo(UpdateStaffInfoDTO updateStaffInfo);
}
