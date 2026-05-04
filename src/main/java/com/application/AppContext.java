package com.application;

import com.application.service.*;
import lombok.Getter;

@Getter
public class AppContext {
    private final SessionService sessionService;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final SeatService seatService;
    private final RoomService roomService;
    private final MovieService movieService;
    private final ShowTimeService showTimeService;
    private final BookingService bookingService;
    private final InvoiceService invoiceService;

    public AppContext(
            SessionService sessionService,
            UserService userService,
            AuthenticationService authenticationService,
            SeatService seatService,
            RoomService roomService,
            MovieService movieService,
            ShowTimeService showTimeService,
            BookingService bookingService,
            InvoiceService invoiceService
    ) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.seatService = seatService;
        this.roomService = roomService;
        this.movieService = movieService;
        this.showTimeService = showTimeService;
        this.bookingService = bookingService;
        this.invoiceService = invoiceService;
    }
}