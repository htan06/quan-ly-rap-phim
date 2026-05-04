package com.application.view;

import com.application.AppContext;
import com.application.dto.auth.UserLoginRequest;
import com.application.entity.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends BaseFrame {

    private static final Color DARK_GOLD = new Color(184, 134, 11);
    private static final Color HOVER_GOLD = new Color(218, 165, 32);
    private final AppContext context;

    public LoginFrame(AppContext appContext) {
        super("Hệ thống Quản lý - Đăng nhập");
        this.context = appContext;

        // Cấu hình cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        // Container Form
        JPanel formContainer = new JPanel();
        formContainer.setBackground(Color.WHITE);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(new EmptyBorder(20, 30, 20, 30));
        mainPanel.add(formContainer);

        // --- Tiêu đề ---
        JLabel titleLabel = new JLabel("Log in");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(titleLabel);

        formContainer.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitleLabel = new JLabel("Chào mừng bạn quay trở lại!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContainer.add(subtitleLabel);

        formContainer.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Input Fields ---
        formContainer.add(createInputLabel("Username"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextField txtUsername = createStyledTextField();
        formContainer.add(txtUsername);

        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        formContainer.add(createInputLabel("Password"));
        formContainer.add(Box.createRigidArea(new Dimension(0, 5)));
        JPasswordField txtPassword = createStyledPasswordField();
        formContainer.add(txtPassword);

        formContainer.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Login Button ---
        JButton loginBtn = new JButton("Log in");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(DARK_GOLD);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hiệu ứng Hover
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { loginBtn.setBackground(HOVER_GOLD); }
            @Override
            public void mouseExited(MouseEvent e) { loginBtn.setBackground(DARK_GOLD); }
        });

        // Xử lý sự kiện Login
        loginBtn.addActionListener(e -> performLogin(txtUsername, txtPassword));

        // Hỗ trợ nhấn Enter để đăng nhập
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performLogin(txtUsername, txtPassword);
            }
        };
        txtUsername.addKeyListener(enterKey);
        txtPassword.addKeyListener(enterKey);

        formContainer.add(loginBtn);
    }

    private void performLogin(JTextField txtUsername, JPasswordField txtPassword) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ tài khoản và mật khẩu!");
            return;
        }

        // 1. Gọi Service xác thực
        UserLoginRequest request = new UserLoginRequest(username, password);
        boolean isSuccess = context.getAuthenticationService().login(request);

        if (isSuccess) {
            // 2. Lấy Session vừa được khởi tạo
            Session session = context.getSessionService().getCurrent();

            if (session != null && session.getRole() != null) {
                this.dispose(); // Đóng form login

                // 3. Điều hướng dựa trên Role Name
                String role = session.getRole().getRoleName();

                if ("MANAGER".equalsIgnoreCase(role)) {
                    new ManagerFrame(context).setVisible(true);
                } else {
                    new StaffFrame(context).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy thông tin phiên đăng nhập!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    // --- Helper Methods ---

    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(50, 50, 50));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
}