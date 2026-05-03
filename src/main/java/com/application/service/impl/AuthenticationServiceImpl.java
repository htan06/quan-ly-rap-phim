package com.application.service.impl;

import com.application.dao.UserDao;
import com.application.dto.auth.UserLoginRequest;
import com.application.dto.user.CreateStaffDTO;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;
import com.application.service.AuthenticationService;
import com.application.service.SessionService;
import com.application.utils.ValidData;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationServiceImpl implements AuthenticationService {

    private UserDao userDao;
    private SessionService sessionService;

    public AuthenticationServiceImpl(UserDao userDao, SessionService sessionService) {
        this.userDao = userDao;
        this.sessionService = sessionService;
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
    public void changePassword(String username, String newPassword) {
        String hashPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        userDao.changePassword(username, hashPassword);
    }

    @Override
    public void setRoleUser(Long userId, Integer roleId) {
        userDao.setRole(userId, roleId);
    }

    @Override
    public boolean login(UserLoginRequest userLogin) {
        User user = userDao.findByUserName(userLogin.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (BCrypt.checkpw(userLogin.password(), user.getPassword())) {
            sessionService.createSession(user);
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        sessionService.removeSession();
    }
}
