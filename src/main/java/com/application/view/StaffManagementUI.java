package com.application.view;

import com.application.view.controller.NavigationController;

import javax.swing.*;
import java.awt.*;

public class StaffManagementUI extends JFrame {

    private final NavigationController navigationController;

    public StaffManagementUI(NavigationController navigationController) {
        this.navigationController = navigationController;
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("Quản lý - Dashboard");
        setSize(800, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initUI() {
        add(buildSidebar(), BorderLayout.WEST);

        JPanel content = new JPanel(new GridLayout(3, 2, 15, 15));
        content.setBackground(Color.decode("#E7E6E6"));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        content.add(menuCard("🎬 Quản lý phim",    e -> navigationController.navigateToMovieManagement()));
        content.add(menuCard("⏱ Quản lý suất chiếu", e -> navigationController.navigateToShowTimeManagement()));
        content.add(menuCard("👤 Nhân viên",        e -> {}));
        content.add(menuCard("💰 Doanh thu",        e -> navigationController.navigateToRevenueReport()));
        content.add(menuCard("📊 Thống kê",         e -> {}));
        content.add(menuCard("🚪 Đăng xuất",        e -> navigationController.logout()));

        add(content, BorderLayout.CENTER);
    }

    private JButton menuCard(String label, java.awt.event.ActionListener action) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel(new GridLayout(6, 1, 0, 20));
        p.setPreferredSize(new Dimension(70, 500));
        p.setBackground(Color.decode("#F4BC05"));
        String[] labels = {"🏠","🎬","⏱","👤","💰","📊"};
        for (String lbl : labels) {
            JButton btn = new JButton(lbl);
            btn.setFocusPainted(false); btn.setBorderPainted(false);
            btn.setBackground(Color.decode("#F4BC05")); btn.setForeground(Color.WHITE);
            p.add(btn);
        }
        return p;
    }
}