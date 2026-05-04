package com.application.view;

import com.application.AppContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StaffFrame extends BaseFrame {

    private final AppContext context;

    public StaffFrame(AppContext context) {
        super("Staff Dashboard");
        this.context = context;

        // --- Header (Chứa Tab và Logout) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        // Tên ứng dụng/Logo bên trái
        JLabel logo = new JLabel("  STAFF PORTAL");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(ThemeConfig.PRIMARY);
        header.add(logo, BorderLayout.WEST);

        // TabbedPane ở giữa
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFocusable(false);

        // Thêm các panel đã thiết kế
        tabbedPane.addTab("  Bán vé  ", new SellTicketPanel(context));
        tabbedPane.addTab("  Lịch sử giao dịch  ", new HistoryPanel(context));

        // Nút Logout bên phải header
        JButton logoutBtn = new JButton("Đăng xuất");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setForeground(new Color(220, 53, 69)); // Đỏ nhã nhặn
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorder(new EmptyBorder(0, 20, 0, 20));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setFocusPainted(false);

        logoutBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame(context).setVisible(true);
            }
        });

        // Ghép Logout vào Header
        header.add(logoutBtn, BorderLayout.EAST);

        // Layout chính
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Điều chỉnh lại nền của tabbedPane để khớp với Theme
        UIManager.put("TabbedPane.background", Color.WHITE);
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.borderHighlightColor", Color.WHITE);
        SwingUtilities.updateComponentTreeUI(tabbedPane);
    }
}