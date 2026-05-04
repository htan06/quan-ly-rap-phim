package com.application.view;

import com.application.AppContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManagerFrame extends BaseFrame {

    private final AppContext context;
    private CardLayout cardLayout = new CardLayout();
    private JPanel content = new JPanel(cardLayout);

    public ManagerFrame(AppContext context) {
        super("Manager Dashboard");
        this.context = context;

        // --- Sidebar ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBackground(new Color(248, 249, 250)); // Off-white
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

        // Header Sidebar (Logo/Tên)
        JLabel logo = new JLabel("CINEMAS", JLabel.CENTER);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(ThemeConfig.PRIMARY);
        logo.setBorder(new EmptyBorder(30, 0, 30, 0));
        sidebar.add(logo, BorderLayout.NORTH);

        // Menu buttons container
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(248, 249, 250));
        menu.setBorder(new EmptyBorder(0, 10, 0, 10));

        // --- Các nút chức năng Sidebar ---
        addMenuButton(menu, "Quản lý tài khoản", "ACCOUNT");
        addMenuButton(menu, "Quản lý phim", "MOVIE");
        addMenuButton(menu, "Quản lý phòng", "ROOM"); // Thêm nút phòng
        addMenuButton(menu, "Lịch chiếu", "SHOWTIME");

        // --- Logout Button ---
        JButton logoutBtn = new JButton("Đăng xuất");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setForeground(new Color(220, 53, 69));
        logoutBtn.setBackground(new Color(248, 249, 250));
        logoutBtn.setBorder(new EmptyBorder(15, 0, 15, 0));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                context.getAuthenticationService().logout();
                dispose();
                new LoginFrame(context).setVisible(true);
            }
        });

        sidebar.add(menu, BorderLayout.CENTER);
        sidebar.add(logoutBtn, BorderLayout.SOUTH);

        // --- Content Area (Vùng hiển thị các Panel) ---
        content.setBackground(Color.WHITE);

        // Đăng ký các Panel vào CardLayout
        content.add(new AccountPanel(context), "ACCOUNT");
        content.add(new MoviePanel(context), "MOVIE");
        content.add(new RoomPanel(context), "ROOM"); // Thêm RoomPanel đã viết
        content.add(new ShowTimePanel(context), "SHOWTIME");

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        // Hiển thị tab mặc định
        cardLayout.show(content, "ACCOUNT");
    }

    private void addMenuButton(JPanel panel, String name, String key) {
        JButton btn = new JButton(name);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(64, 64, 64));
        btn.setBackground(new Color(248, 249, 250));
        btn.setBorder(new EmptyBorder(0, 20, 0, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hiệu ứng Hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(230, 230, 230));
                btn.setForeground(ThemeConfig.PRIMARY);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(248, 249, 250));
                btn.setForeground(new Color(64, 64, 64));
            }
        });

        // Chuyển card khi click
        btn.addActionListener(e -> cardLayout.show(content, key));

        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
}