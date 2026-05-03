package com.application.service;

import com.application.dto.user.UpdateStaffInfoDTO;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id);

    User getUserByUsername(String username);

    void updateStatusUser(String username, UserStatus status);

    void updateUserInfo(UpdateStaffInfoDTO updateStaffInfo);
}
