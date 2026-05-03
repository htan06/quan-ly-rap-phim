package com.application.service;

import com.application.entity.User;

public interface SessionService {
    String createSession(User user);
    void removeSession();
    String getSessionId();
}
