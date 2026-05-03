package com.application.service.impl;

import com.application.dao.UserDao;
import com.application.dto.user.UpdateStaffInfoDTO;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;
import com.application.service.UserService;
import com.application.utils.ValidData;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void updateStatusUser(String username, UserStatus status) {
        userDao.findByUserName(username);
        userDao.updateStatus(username, status);
    }

    @Override
    public void updateUserInfo(UpdateStaffInfoDTO updateStaffInfo) {
        userDao.findByUserName(updateStaffInfo.username());
        if (!ValidData.validEmail(updateStaffInfo.email())) {
            throw new IllegalArgumentException("Email khong dung dinh dang");
        }

        if (!ValidData.validPhoneNumber(updateStaffInfo.phoneNumber())) {
            throw new IllegalArgumentException("So dien thoai khong dung dinh dang");
        }
        userDao.updateInfo(updateStaffInfo);
    }
}
