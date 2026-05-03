package com.application.service;

import com.application.dao.InvoiceDao;
import com.application.entity.Invoice;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceDao invoiceDao;

    public void exportInvoice(Long bookingId, String path) {

        Invoice invoice = invoiceDao.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));


        String fileName = path + "/invoice_" + bookingId + ".txt";

        StringBuilder sb = new StringBuilder();

        sb.append("===== CINEMAS =====\n\n");

        sb.append("Movie: ").append(invoice.getMovieTitle()).append("\n");
        sb.append("Room: ").append(invoice.getRoomName()).append("\n");
        sb.append("Time: ").append(invoice.getStartTime())
                .append(" - ")
                .append(invoice.getEndTime())
                .append("\n\n");

        sb.append("Seats: ");
        invoice.getSeats().forEach(s -> sb.append(s).append(" "));
        sb.append("\n\n");

        sb.append("Tax: ").append(invoice.getTotalTax()).append("\n");
        sb.append("Total: ").append(invoice.getTotalPrice()).append("\n");
        sb.append("Staff: ").append(invoice.getStaffName()).append("\n");

        try {
            java.nio.file.Files.writeString(
                    java.nio.file.Path.of(fileName),
                    sb.toString()
            );
        } catch (Exception e) {
            throw new RuntimeException("Write invoice failed", e);
        }
    }
}
