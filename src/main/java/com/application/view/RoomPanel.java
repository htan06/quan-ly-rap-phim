package com.application.view;

import com.application.AppContext;
import com.application.entity.Room;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomPanel extends JPanel {
    private final AppContext context;
    private DefaultTableModel tableModel;

    public RoomPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- Tiêu đề và Nút làm mới ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Danh sách phòng chiếu");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnRefresh = new JButton("Làm mới dữ liệu");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setBackground(ThemeConfig.PRIMARY);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadData());

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);

        // --- Bảng dữ liệu ---
        String[] columns = {"ID", "Tên Phòng", "Loại Phòng", "Sức Chứa", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadData(); // Tải dữ liệu khi mở
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try {
            List<Room> rooms = context.getRoomService().findAll();
            for (Room r : rooms) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        r.getName(),
                        r.getRoomType(),
                        r.getCapacity(),
                        r.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }
}