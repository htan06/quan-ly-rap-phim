package com.application.service.impl;

import com.application.dao.UserDao;
import com.application.dto.UpdateStaffInfoDTO;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;
import com.application.service.UserService;
import com.application.utils.ValidData;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateStatusUser(String username, UserStatus status) {
        userDao.updateStatus(username, status);
    }

    @Override
    public void updateUserInfo(UpdateStaffInfoDTO updateStaffInfo) {
        if (!ValidData.validEmail(updateStaffInfo.email())) {
            throw new IllegalArgumentException("Email khong dung dinh dang");
        }

        if (!ValidData.validPhoneNumber(updateStaffInfo.phoneNumber())) {
            throw new IllegalArgumentException("So dien thoai khong dung dinh dang");
        }
        userDao.updateInfo(updateStaffInfo);
    }
}
