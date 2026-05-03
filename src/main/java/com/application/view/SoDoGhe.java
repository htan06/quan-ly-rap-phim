package com.application.view;

import com.application.entity.Seat;
import com.application.entity.ShowTime;
import com.application.service.BookingService;
import com.application.service.ShowTimeService;
import com.application.view.controller.BookingController;
import com.application.view.controller.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SoDoGhe extends JFrame {

    private final NavigationController navigationController;
    private final BookingController bookingController;
    private final ShowTimeService showTimeService;
    private final BookingService bookingService;
    private final Long showTimeId;
    private final List<Long> selectedSeatIds = new ArrayList<>();

    // Booking flow
    public SoDoGhe(Long showTimeId,
                   ShowTimeService showTimeService,
                   BookingService bookingService,
                   NavigationController navigationController,
                   BookingController bookingController) {
        this.showTimeId = showTimeId;
        this.showTimeService = showTimeService;
        this.bookingService = bookingService;
        this.navigationController = navigationController;
        this.bookingController = bookingController;
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("Chọn ghế");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(Color.decode("#E7E6E6"));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("Sơ đồ ghế - Chọn ghế của bạn", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(title, BorderLayout.NORTH);

        // Load seats from service
        List<Seat> seats = new ArrayList<>();
        List<Long> bookedSeatIds = new ArrayList<>();
        String roomName = "";
        try {
            ShowTime showTime = showTimeService.findById(showTimeId);
            if (showTime != null && showTime.getRoom() != null) {
                roomName = showTime.getRoom().getName();
                if (showTime.getRoom().getSeats() != null)
                    seats = showTime.getRoom().getSeats();
            }
            bookedSeatIds = bookingService.seatBookedId(showTimeId);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải thông tin ghế!");
        }

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(5, 10));
        centerPanel.setBackground(Color.decode("#E7E6E6"));

        // Screen
        JPanel screenPanel = new JPanel(new BorderLayout());
        screenPanel.setBackground(Color.DARK_GRAY);
        screenPanel.setPreferredSize(new Dimension(600, 30));
        JLabel screenLabel = new JLabel("MÀN HÌNH - " + roomName, SwingConstants.CENTER);
        screenLabel.setForeground(Color.WHITE);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 12));
        screenPanel.add(screenLabel, BorderLayout.CENTER);
        centerPanel.add(screenPanel, BorderLayout.NORTH);

        // Seat grid — tính số cột dựa trên capacity để layout hợp lý
        int total = seats.size();
        int cols = total > 0 ? (int) Math.ceil(Math.sqrt(total)) : 10;
        int rows = total > 0 ? (int) Math.ceil((double) total / cols) : 5;

        JPanel seatPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        seatPanel.setBackground(Color.decode("#E7E6E6"));

        for (Seat seat : seats) {
            boolean isBooked = bookedSeatIds.contains(seat.getId());

            JToggleButton btn = new JToggleButton(seat.getName());
            btn.setPreferredSize(new Dimension(55, 35));
            btn.setFont(new Font("Arial", Font.PLAIN, 10));
            btn.setFocusPainted(false);

            if (isBooked) {
                btn.setBackground(Color.GRAY);
                btn.setEnabled(false);
            } else {
                btn.setBackground(Color.WHITE);
                final Long seatId = seat.getId();
                btn.addItemListener(ev -> {
                    if (ev.getStateChange() == ItemEvent.SELECTED) {
                        btn.setBackground(Color.decode("#F4BC05"));
                        selectedSeatIds.add(seatId);
                    } else {
                        btn.setBackground(Color.WHITE);
                        selectedSeatIds.remove(seatId);
                    }
                });
            }

            seatPanel.add(btn);
        }

        // Nếu không đủ ô để fill grid, thêm ô trống
        int remaining = rows * cols - total;
        for (int i = 0; i < remaining; i++) {
            JLabel empty = new JLabel();
            seatPanel.add(empty);
        }

        JScrollPane seatScroll = new JScrollPane(seatPanel);
        seatScroll.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(seatScroll, BorderLayout.CENTER);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        legend.setBackground(Color.decode("#E7E6E6"));
        legend.add(makeLegend(Color.WHITE,               "Trống"));
        legend.add(makeLegend(Color.decode("#F4BC05"),   "Đã chọn"));
        legend.add(makeLegend(Color.GRAY,                "Đã đặt"));
        centerPanel.add(legend, BorderLayout.SOUTH);

        main.add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.decode("#E7E6E6"));

        JButton btnBack = new JButton("← Quay lại");
        btnBack.addActionListener(e -> bookingController.goBackToShowTimes());
        btnPanel.add(btnBack);

        JButton btnConfirm = new JButton("Xác nhận");
        btnConfirm.setBackground(Color.decode("#F4BC05"));
        btnConfirm.addActionListener(e -> {
            if (selectedSeatIds.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!");
                return;
            }
            bookingController.selectSeats(new ArrayList<>(selectedSeatIds));
        });
        btnPanel.add(btnConfirm);

        main.add(btnPanel, BorderLayout.SOUTH);
        add(main);
    }

    private JPanel makeLegend(Color color, String label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setBackground(Color.decode("#E7E6E6"));
        JPanel box = new JPanel();
        box.setBackground(color);
        box.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        box.setPreferredSize(new Dimension(15, 15));
        p.add(box);
        p.add(new JLabel(label));
        return p;
    }
}