package com.application.service;

import com.application.dao.UserDao;
import com.application.model.User;

import java.util.List;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void printUsers() {
        List<User> users = userDao.getListUser();
        users.forEach(System.out::println);
    }
}
