package com.application.view;

import com.application.view.controller.NavigationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThongKeDoanhThuUI extends JFrame {

    private final NavigationController navigationController;

    public ThongKeDoanhThuUI(NavigationController navigationController) {
        this.navigationController = navigationController;
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle("Thống kê doanh thu");
        setSize(800, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(Color.decode("#E7E6E6"));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Thống kê doanh thu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        main.add(title, BorderLayout.NORTH);

        String[] cols = {"Tháng", "Số vé", "Doanh thu (VNĐ)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(24);
        // Placeholder data
        for (int m = 1; m <= 12; m++)
            model.addRow(new Object[]{"Tháng " + m, 0, "0"});

        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnBack = new JButton("← Quay lại");
        btnBack.addActionListener(e -> navigationController.navigateToManagerDashboard());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(Color.decode("#E7E6E6"));
        btnPanel.add(btnBack);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
    }
}