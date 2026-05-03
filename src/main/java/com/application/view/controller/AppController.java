package com.application.view.controller;

import com.application.dto.auth.UserLoginRequest;
import com.application.service.AuthenticationService;
import com.application.service.SessionService;

public class AppController {

    private final AuthenticationService authenticationService;
    private final SessionService sessionService;
    private final NavigationController navigationController;

    public AppController(AuthenticationService authenticationService,
                         SessionService sessionService,
                         NavigationController navigationController) {
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
        this.navigationController = navigationController;
    }

    public void initializeApp() {
        navigationController.navigateToLogin();
    }

    public boolean login(String username, String password) {
        try {
            boolean ok = authenticationService.login(new UserLoginRequest(username, password));
            if (ok) navigationController.navigateAfterLogin();
            return ok;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void logout() {
        navigationController.logout();
    }

    public String getCurrentUserRole() {
        return sessionService.getCurrent().getRole().getRoleName();
    }

    public boolean isUserAuthenticated() {
        return sessionService.getSessionId() != null;
    }
}