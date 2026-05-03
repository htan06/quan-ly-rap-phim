// BookingController.java

package com.application.view.controller;

import com.application.dto.booking.CreateBookingDTO;
import com.application.service.BookingService;
import com.application.service.InvoiceService;
import com.application.service.SessionService;

import java.util.ArrayList;
import java.util.List;

public class BookingController {

    private final NavigationController navigationController;
    private final BookingService bookingService;
    private final SessionService sessionService;
    private final InvoiceService invoiceService;

    private Long selectedMovieId;
    private Long selectedShowTimeId;
    private List<Long> selectedSeatIds = new ArrayList<>();
    private Long bookingId;

    public BookingController(NavigationController navigationController,
                             BookingService bookingService,
                             SessionService sessionService,
                             InvoiceService invoiceService) {
        this.navigationController = navigationController;
        this.bookingService = bookingService;
        this.sessionService = sessionService;
        this.invoiceService = invoiceService;
    }

    public void selectMovie(Long movieId) {
        this.selectedMovieId = movieId;
        navigationController.navigateToShowTime(movieId);
    }

    public void selectShowTime(Long showTimeId) {
        this.selectedShowTimeId = showTimeId;
    }

    public void selectSeats(List<Long> seatIds) {
        this.selectedSeatIds = seatIds;
        navigationController.navigateToBookingManagement();
    }

    public void createBooking(Long membershipId) {
        try {
            Long staffId = sessionService.getCurrent().getUserId(); // lấy từ session

            CreateBookingDTO dto = new CreateBookingDTO(
                    selectedShowTimeId,
                    membershipId,   // null nếu không dùng
                    staffId,
                    selectedSeatIds
            );

            bookingId = bookingService.create(dto);
            invoiceService.exportInvoice(bookingId, "D:/cinemas/invoice");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void completeBooking() {
        resetState();
        navigationController.navigateToStaffDashboard();
    }

    public void cancelBooking() {
        resetState();
        navigationController.navigateToStaffDashboard();
    }

    public void goBackToMovies()    { navigationController.navigateToStaffDashboard(); }
    public void goBackToShowTimes() { navigationController.navigateToShowTime(selectedMovieId); }
    public void goBackToSeats()     { navigationController.navigateToSeatSelection(selectedShowTimeId); }

    private void resetState() {
        selectedMovieId    = null;
        selectedShowTimeId = null;
        selectedSeatIds    = new ArrayList<>();
        bookingId          = null;
    }

    public Long getSelectedMovieId()       { return selectedMovieId; }
    public Long getSelectedShowTimeId()    { return selectedShowTimeId; }
    public List<Long> getSelectedSeatIds() { return selectedSeatIds; }
    public Long getBookingId()             { return bookingId; }
}