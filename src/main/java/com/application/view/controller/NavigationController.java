package com.application.view.controller;

import com.application.service.*;
import com.application.view.*;

import javax.swing.*;

public class NavigationController {

    private final MovieService movieService;
    private final ShowTimeService showTimeService;
    private final SeatService seatService;
    private final BookingService bookingService;
    private final AuthenticationService authenticationService;
    private final SessionService sessionService;
    private final InvoiceService invoiceService;

    private BookingController bookingController;
    private JFrame currentFrame;

    public NavigationController(MovieService movieService,
                                ShowTimeService showTimeService,
                                SeatService seatService,
                                BookingService bookingService,
                                AuthenticationService authenticationService,
                                SessionService sessionService,
                                InvoiceService invoiceService) {
        this.movieService = movieService;
        this.showTimeService = showTimeService;
        this.seatService = seatService;
        this.bookingService = bookingService;
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
        this.invoiceService = invoiceService;
        this.bookingController = new BookingController(this, bookingService, sessionService, invoiceService);
    }

    private void switchTo(JFrame newFrame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        currentFrame = newFrame;
        currentFrame.setVisible(true);
    }

    public void navigateToLogin() {
        switchTo(new LoginUI(authenticationService, this));
    }

    public void navigateAfterLogin() {
        String role = sessionService.getCurrent().getRole().getRoleName();
        if ("MANAGER".equalsIgnoreCase(role)) {
            navigateToManagerDashboard();
        } else {
            navigateToStaffDashboard();
        }
    }

    public void navigateToStaffDashboard() {
        switchTo(new ListMovieUI(movieService, this, bookingController));
    }

    public void navigateToManagerDashboard() {
        switchTo(new StaffManagementUI(this));
    }

    public void navigateToShowTime(Long movieId) {
        switchTo(new ShowTimeUI(movieId, showTimeService, this, bookingController));
    }

    public void navigateToSeatSelection(Long showTimeId) {
        switchTo(new SoDoGhe(showTimeId, showTimeService, bookingService, this, bookingController));
    }

    public void navigateToBookingManagement() {
        switchTo(new BookingManagementUI(bookingService, this, bookingController));
    }

    public void navigateToMovieManagement() {
        switchTo(new ListMovieUI(movieService, this));
    }

    public void navigateToShowTimeManagement() {
        switchTo(new ShowTimeUI(showTimeService, this));
    }

    public void navigateToRevenueReport() {
        switchTo(new ThongKeDoanhThuUI(this));
    }

    public void logout() {
        sessionService.removeSession();
        bookingController = new BookingController(this, bookingService, sessionService, invoiceService);
        navigateToLogin();
    }

    public BookingController getBookingController() {
        return bookingController;
    }

    public SessionService getSessionService() {
        return sessionService;
    }
}