package com.application.view;

import com.application.service.*;
import com.application.view.controller.*;

public class AppUI {

    private AppController appController;
    private NavigationController navigationController;

    private final BookingService bookingService;
    private final UserService userService;
    private final MovieService movieService;
    private final AuthenticationService authenticationService;
    private final SessionService sessionService;
    private final ShowTimeService showTimeService;
    private final SeatService seatService;
    private final RoomService roomService;
    private final MembershipService membershipService;
    private final InvoiceService invoiceService;

    // Constructor đầy đủ
    public AppUI(BookingService bookingService,
                 UserService userService,
                 MovieService movieService,
                 AuthenticationService authenticationService,
                 SessionService sessionService,
                 ShowTimeService showTimeService,
                 SeatService seatService,
                 RoomService roomService,
                 MembershipService membershipService,
                 InvoiceService invoiceService) {

        this.bookingService = bookingService;
        this.userService = userService;
        this.movieService = movieService;
        this.authenticationService = authenticationService;
        this.sessionService = sessionService;
        this.showTimeService = showTimeService;
        this.seatService = seatService;
        this.roomService = roomService;
        this.membershipService = membershipService;
        this.invoiceService = invoiceService;
    }

    public void start() {
        initializeControllers();
        appController.initializeApp();
    }

    private void initializeControllers() {
        // Đúng thứ tự với NavigationController constructor:
        // MovieService, ShowTimeService, SeatService, BookingService, AuthService, SessionService
        navigationController = new NavigationController(
                movieService,
                showTimeService,
                seatService,        // ← fix: đúng vị trí
                bookingService,     // ← fix: đúng vị trí
                authenticationService,
                sessionService,
                invoiceService
        );

        appController = new AppController(
                authenticationService,
                sessionService,
                navigationController
        );
    }

    public AppController getAppController()           { return appController; }
    public NavigationController getNavigationController() { return navigationController; }
}