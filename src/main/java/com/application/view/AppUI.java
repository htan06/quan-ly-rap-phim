package com.application.view;

import com.application.service.AuthenticationService;
import com.application.service.BookingService;
import com.application.service.MovieService;
import com.application.service.UserService;

import javax.swing.*;

public class AppUI {
    private final BookingService bookingService;
    private final UserService userService;
    private final MovieService movieService;
    private final AuthenticationService authenticationService;

    private JFrame currentFrame;

    public AppUI(BookingService bookingService,
                 UserService userService,
                 MovieService movieService,
                 AuthenticationService authenticationService) {

        this.bookingService = bookingService;
        this.userService = userService;
        this.movieService = movieService;
        this.authenticationService = authenticationService;
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            openLoginUI();
        });
    }

    // ===== Navigation =====

    public void openLoginUI() {
        switchFrame(new LoginUI(authenticationService));
    }

    // sau này thêm:
    // public void openMovieManagement() { ... }
    // public void openUserManagement() { ... }

    // ===== Core điều hướng =====

    private void switchFrame(JFrame newFrame) {
        if (currentFrame != null) {
            currentFrame.dispose(); // đóng màn hình cũ
        }

        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }
}
