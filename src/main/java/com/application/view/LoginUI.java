package com.application.view;

import com.application.dto.auth.UserLoginRequest;
import com.application.service.AuthenticationService;
import com.application.view.controller.NavigationController;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private final AuthenticationService authenticationService;
    private final NavigationController navigationController;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginUI(AuthenticationService authenticationService,
                   NavigationController navigationController) {
        this.authenticationService = authenticationService;
        this.navigationController = navigationController;
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("Đăng nhập");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initUI() {
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(Color.decode("#F4BC05"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("CINEMA BOOKING", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        main.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        JLabel lblUser = new JLabel("Tài khoản:");
        gbc.gridx = 0; gbc.gridy = 1;
        main.add(lblUser, gbc);

        txtUsername = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        main.add(txtUsername, gbc);

        JLabel lblPass = new JLabel("Mật khẩu:");
        gbc.gridx = 0; gbc.gridy = 2;
        main.add(lblPass, gbc);

        txtPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        main.add(txtPassword, gbc);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        main.add(btnLogin, gbc);

        add(main);

        btnLogin.addActionListener(e -> handleLogin());
        txtPassword.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try {
            boolean ok = authenticationService.login(new UserLoginRequest(username, password));
            if (ok) {
                navigationController.navigateAfterLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
                txtPassword.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng nhập: " + ex.getMessage());
        }
    }
}