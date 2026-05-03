package com.application.service.impl;

import com.application.entity.Session;
import com.application.entity.User;
import com.application.service.SessionService;
import com.application.utils.SecureRandomId;

public class SessionServiceImpl implements SessionService {

    private Session session;

    @Override
    public String createSession(User user) {

        session = Session.builder()
                .id(SecureRandomId.randomString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

        return session.getId();
    }

    @Override
    public void removeSession() {
        session = null;
    }

    @Override
    public String getSessionId() {
        return session.getId();
    }
}
