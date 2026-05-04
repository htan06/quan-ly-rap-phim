package com.application.service;

import com.application.entity.Session;
import com.application.entity.User;

public interface SessionService {
    String createSession(User user);
    void removeSession();
    Session getCurrent();
}
