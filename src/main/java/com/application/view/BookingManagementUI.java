package com.application.view;

import com.application.service.BookingService;
import com.application.view.controller.BookingController;
import com.application.view.controller.NavigationController;

import javax.swing.*;
import java.awt.*;

public class BookingManagementUI extends JFrame {

    private final BookingService bookingService;
    private final NavigationController navigationController;
    private final BookingController bookingController;

    private JCheckBox chkMembership;

    public BookingManagementUI(BookingService bookingService,
                               NavigationController navigationController,
                               BookingController bookingController) {
        this.bookingService = bookingService;
        this.navigationController = navigationController;
        this.bookingController = bookingController;
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("Xác nhận đặt vé");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(Color.decode("#E7E6E6"));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Xác nhận đặt vé", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        main.add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBackground(Color.decode("#E7E6E6"));

        infoPanel.add(new JLabel("Phim:"));
        infoPanel.add(new JLabel(String.valueOf(bookingController.getSelectedMovieId())));
        infoPanel.add(new JLabel("Suất chiếu:"));
        infoPanel.add(new JLabel(String.valueOf(bookingController.getSelectedShowTimeId())));
        infoPanel.add(new JLabel("Ghế đã chọn:"));
        infoPanel.add(new JLabel(bookingController.getSelectedSeatIds().toString()));

        chkMembership = new JCheckBox("Dùng thẻ thành viên");
        infoPanel.add(chkMembership);
        infoPanel.add(new JLabel(""));

        main.add(infoPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.decode("#E7E6E6"));

        JButton btnBack   = new JButton("← Quay lại");
        JButton btnCancel = new JButton("Huỷ");
        JButton btnConfirm = new JButton("Xác nhận đặt vé");
        btnConfirm.setBackground(Color.decode("#F4BC05"));

        btnBack.addActionListener(e -> bookingController.goBackToSeats());
        btnCancel.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Huỷ đặt vé?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) bookingController.cancelBooking();
        });
        btnConfirm.addActionListener(e -> {
            // Hỏi dùng membership không
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có muốn dùng thẻ thành viên không?",
                    "Thẻ thành viên",
                    JOptionPane.YES_NO_OPTION
            );

            // YES → cần membershipId thật, hiện tại để null là placeholder
            // Nếu có MembershipService thì truyền id vào đây
            Long membershipId = (choice == JOptionPane.YES_OPTION) ? 1L : null;

            bookingController.createBooking(membershipId);

            JOptionPane.showMessageDialog(
                    this,
                    "Đặt vé thành công! Mã đặt vé: " + bookingController.getBookingId()
            );
            bookingController.completeBooking();
        });

        btnPanel.add(btnBack); btnPanel.add(btnCancel); btnPanel.add(btnConfirm);
        main.add(btnPanel, BorderLayout.SOUTH);
        add(main);
    }
}