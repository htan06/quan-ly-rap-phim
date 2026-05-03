package com.application.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginUI extends JFrame {

	private JTextField userField;
	private JPasswordField passwordField;
	private JButton loginButton;

	public LoginUI() {
		initFrame();
		initComponents();
		initUI();
	}
	
	private void initFrame() {
		setTitle("Login");
		setSize(600, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	private void initComponents() {
		userField = new JTextField();
		passwordField = new JPasswordField();
		loginButton = new JButton("Đăng nhập");
	}
	
	private void initUI() {
		// nền
		JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(new Color(30, 30, 30));
        
        // card (form)
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 250));
        card.setBackground(new Color(45, 45, 45));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // title
        JLabel title = new JLabel("ABSOLUTE CINEMA");
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // căn giữa
        title.setForeground(new Color(255, 140, 0));
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        // username
        JLabel userLabel = new JLabel("Tên đăng nhập");
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleInput(userField);
        userField.setToolTipText("Nhập tên đăng nhập của bạn");
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // cho phép mở rộng chiều ngang
        
        //password
        JLabel passLabel = new JLabel("Mật khẩu");
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleInput(passwordField);
        passwordField.setToolTipText("Mật khau phải có ít nhất 6 ký tự");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // cho phép mở rộng chiều ngang
        
    	// button
        styleButton(loginButton);
        loginButton.addActionListener(e -> {
			if(validateInput()) {
				JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
				}
			});
       
        // thêm vào card
        card.add(title);
        card.add(Box.createVerticalStrut(20)); // khoảng cách giữa title và input
        
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(userField);
        
        card.add(Box.createVerticalStrut(10)); // khoảng cách giữa username và password
        
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        
        card.add(Box.createVerticalStrut(20)); // khoảng cách giữa password và button
        
        card.add(loginButton);
        
        bg.add(card);
        add(bg);
	}

	private void styleButton(JButton btn) {
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(new Color(255, 140, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);// tắt viền khi click
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // đổi con trỏ chuột khi hover
	}

	private void styleInput(JTextField field) {
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
	}
	
	private boolean validateInput() {
		String username = userField.getText().trim();
		String password = new String(passwordField.getPassword()).trim();
		
		if(username.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập");
			userField.requestFocus();
			return false;
		}
		
		if(password.length() < 6) {
			JOptionPane.showMessageDialog(this, "Mật khâu phải có ít nhất 6 ký tự");
			passwordField.requestFocus();
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		new LoginUI().setVisible(true);
	}
}
