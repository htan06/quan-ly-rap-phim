package com.application.view;

import com.application.AppContext;
import com.application.entity.Booking;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private final AppContext context;
    private final DefaultTableModel tableModel;

    public HistoryPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout(10, 10));
        setBackground(ThemeConfig.BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel title = new JLabel("Lịch sử giao dịch");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadHistory());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(btnRefresh, BorderLayout.EAST);

        // Table
        String[] cols = {"ID", "Thời gian", "Phim", "Tổng tiền", "Trạng thái", "Nhân viên"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

//        loadHistory();
    }

    private void loadHistory() {
        tableModel.setRowCount(0);
        List<Booking> bookings = context.getBookingService().findAll();
        for (Booking b : bookings) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getCreatedAt(),
                    b.getShowTime().getMovie().getTitle(),
                    String.format("%,.0f VNĐ", b.getTotalPrice()),
                    b.getStatus(),
                    b.getStaff().getLastName() + " " + b.getStaff().getFirstName()
            });
        }
    }
}