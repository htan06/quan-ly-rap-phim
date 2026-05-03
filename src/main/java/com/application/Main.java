package com.application;

import com.application.dao.*;
import com.application.dto.user.CreateStaffDTO;
import com.application.service.*;
import com.application.service.impl.*;
import com.application.view.AppUI;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection connectionDb = DatabaseConnection.getInstance().getConnection();

        // ===== DAO =====
        RoleDao roleDao = new RoleDao(connectionDb);
        UserDao userDao = new UserDao(connectionDb, roleDao);
        SeatDao seatDao = new SeatDao(connectionDb);
        RoomDao roomDao = new RoomDao(connectionDb, seatDao);
        MovieDao movieDao = new MovieDao(connectionDb);
        ShowTimeDao showTimeDao = new ShowTimeDao(connectionDb);
        BookingDao bookingDao = new BookingDao(connectionDb);
        MembershipDao membershipDao = new MembershipDao(connectionDb);
        InvoiceDao invoiceDao = new InvoiceDao(connectionDb);

        // ===== SERVICE =====
        SessionService sessionService = new SessionServiceImpl();

        UserService userService = new UserServiceImpl(userDao);
        AuthenticationService authenticationService = new AuthenticationServiceImpl(userDao, sessionService);
        SeatService seatService = new SeatServiceImpl(seatDao);
        RoomService roomService = new RoomServiceImpl(roomDao);
        MovieService movieService = new MovieServiceImpl(movieDao);
        ShowTimeService showTimeService = new ShowTimeServiceImpl(showTimeDao);
        BookingService bookingService = new BookingServiceImpl(bookingDao, showTimeService);
        MembershipService membershipService = new MembershipServiceImpl(membershipDao);
        InvoiceService invoiceService = new InvoiceService(invoiceDao);

        AppUI appUI = new AppUI(
                bookingService,
                userService,
                movieService,
                authenticationService,
                sessionService,
                showTimeService,
                seatService,
                roomService,
                membershipService,
                invoiceService
        );

        appUI.start();
    }
}