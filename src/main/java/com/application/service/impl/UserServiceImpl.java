package com.application.service.impl;

import com.application.dao.UserDao;
import com.application.dto.CreateStaffDTO;
import com.application.dto.UpdateStaffInfoDTO;
import com.application.model.User;
import com.application.model.enums.UserStatus;
import com.application.service.UserService;
import com.application.utils.ValidData;
import org.mindrot.jbcrypt.BCrypt;


public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void changePassword(String username, String newPassword) {
        String hashPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        userDao.changePassword(username, hashPassword);
    }

    @Override
    public void createStaff(CreateStaffDTO createStaff) {
        if (!ValidData.validEmail(createStaff.email())) {
            throw new IllegalArgumentException("Email khong dung dinh dang");
        }

        if (!ValidData.validPhoneNumber(createStaff.phoneNumber())) {
            throw new IllegalArgumentException("So dien thoai khong dung dinh dang");
        }

        User user = User.builder()
                .firstName(createStaff.firstName())
                .lastName(createStaff.lastName())
                .email(createStaff.email())
                .phoneNumber(createStaff.phoneNumber())
                .username(createStaff.username())
                .password(BCrypt.hashpw(createStaff.password(), BCrypt.gensalt(10)))
                .status(UserStatus.ACTIVE)
                .build();

        userDao.createUser(user);
    }

    @Override
    public void updateStatusUser(String username, UserStatus status) {
        userDao.updateStatusUser(username, status);
    }

    @Override
    public void updateUserInfo(UpdateStaffInfoDTO updateStaffInfo) {
        if (!ValidData.validEmail(updateStaffInfo.email())) {
            throw new IllegalArgumentException("Email khong dung dinh dang");
        }

        if (!ValidData.validPhoneNumber(updateStaffInfo.phoneNumber())) {
            throw new IllegalArgumentException("So dien thoai khong dung dinh dang");
        }
        userDao.updateUserInfo(updateStaffInfo);
    }
}
