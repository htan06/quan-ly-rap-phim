package com.application.view;

import com.application.AppContext;
import com.application.dto.user.CreateStaffDTO;
import com.application.dto.user.UpdateStaffInfoDTO;
import com.application.entity.User;
import com.application.entity.enums.UserStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AccountPanel extends JPanel {
    private final AppContext context;

    private DefaultTableModel tableModel;
    private JTable table;

    // Components cho Tab Quản lý
    private JTextField txtSearchUsername = createRoundedTextField();
    private User currentSelectedUser;

    // Fields cho Update Info
    private JTextField txtUpdateFirst = createRoundedTextField(), txtUpdateLast = createRoundedTextField(),
            txtUpdateEmail = createRoundedTextField(), txtUpdatePhone = createRoundedTextField();
    private JComboBox<UserStatus> cbStatus = new JComboBox<>(UserStatus.values());
    private JPasswordField txtNewPassword = new JPasswordField();
    // Đã xóa cbRoles

    // Fields cho Create Staff
    private JTextField txtCreateFirst = createRoundedTextField(), txtCreateLast = createRoundedTextField(),
            txtCreateEmail = createRoundedTextField(), txtCreatePhone = createRoundedTextField(),
            txtCreateUser = createRoundedTextField();
    private JPasswordField txtCreatePass = new JPasswordField();

    public AccountPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBackground(ThemeConfig.BACKGROUND);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("  Danh sách tài khoản  ", createListTab());
        tabbedPane.addTab("  Cập nhật & Bảo mật  ", createManageTab());
        tabbedPane.addTab("  Tạo nhân viên mới  ", createAddStaffTab());

        add(tabbedPane, BorderLayout.CENTER);
        loadTableData();
    }

    // --- TAB 1: DANH SÁCH ---
    private JPanel createListTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ThemeConfig.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = {"ID", "Username", "Họ tên", "Email", "Role", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JButton btnRefresh = createStyledButton("Làm mới danh sách", ThemeConfig.PRIMARY);
        btnRefresh.addActionListener(e -> loadTableData());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setBackground(ThemeConfig.BACKGROUND);
        top.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- TAB 2: CẬP NHẬT & BẢO MẬT ---
    private JPanel createManageTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ThemeConfig.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(new RoundedBorder(15));
        searchBox.add(new JLabel("Tìm theo Username:"));
        searchBox.add(txtSearchUsername);
        JButton btnSearch = createStyledButton("Tìm kiếm", ThemeConfig.PRIMARY);
        btnSearch.addActionListener(e -> handleSearch());
        searchBox.add(btnSearch);

        JPanel formsGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        formsGrid.setBackground(ThemeConfig.BACKGROUND);
        formsGrid.add(createUpdateInfoForm());
        formsGrid.add(createSecurityForm());

        JScrollPane scrollPane = new JScrollPane(formsGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(ThemeConfig.BACKGROUND);

        mainPanel.add(searchBox, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createUpdateInfoForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 10, 5); gbc.weightx = 1.0;

        addFormField(panel, "Tên (First Name):", txtUpdateFirst, gbc, 0);
        addFormField(panel, "Họ (Last Name):", txtUpdateLast, gbc, 1);
        addFormField(panel, "Email:", txtUpdateEmail, gbc, 2);
        addFormField(panel, "Số điện thoại:", txtUpdatePhone, gbc, 3);
        addFormField(panel, "Trạng thái hoạt động:", cbStatus, gbc, 4);

        JButton btnSaveInfo = createStyledButton("Cập nhật thông tin", ThemeConfig.PRIMARY);
        btnSaveInfo.addActionListener(e -> handleUpdateInfo());
        gbc.gridy = 5; gbc.insets = new Insets(20, 5, 0, 5);
        panel.add(btnSaveInfo, gbc);

        return panel;
    }

    private JPanel createSecurityForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 10, 5); gbc.weightx = 1.0;

        // Đã loại bỏ phần UI liên quan đến Roles ở đây

        JLabel lblSecurityTitle = new JLabel("Bảo mật tài khoản");
        lblSecurityTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 0; panel.add(lblSecurityTitle, gbc);

        gbc.insets = new Insets(15, 5, 10, 5);
        addFormField(panel, "Mật khẩu mới:", txtNewPassword, gbc, 1);

        JButton btnPass = createStyledButton("Đổi mật khẩu", new Color(40, 167, 69));
        btnPass.addActionListener(e -> handleChangePass());
        gbc.gridy = 2; gbc.insets = new Insets(5, 5, 10, 5);
        panel.add(btnPass, gbc);

        gbc.gridy = 3; gbc.weighty = 1.0; panel.add(new JLabel(""), gbc);
        return panel;
    }

    // --- TAB 3: TẠO STAFF ---
    private JPanel createAddStaffTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConfig.BACKGROUND);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 15, 5); gbc.weightx = 1.0;

        addFormField(card, "Username:", txtCreateUser, gbc, 0);
        addFormField(card, "Mật khẩu:", txtCreatePass, gbc, 1);
        addFormField(card, "Tên:", txtCreateFirst, gbc, 2);
        addFormField(card, "Họ:", txtCreateLast, gbc, 3);
        addFormField(card, "Email:", txtCreateEmail, gbc, 4);
        addFormField(card, "Số điện thoại:", txtCreatePhone, gbc, 5);

        JButton btnCreate = createStyledButton("Tạo tài khoản nhân viên", ThemeConfig.PRIMARY);
        btnCreate.addActionListener(e -> handleCreateStaff());
        gbc.gridy = 6; gbc.insets = new Insets(20, 5, 0, 5);
        card.add(btnCreate, gbc);

        panel.add(card);
        return panel;
    }

    // --- LOGIC ---
    private void loadTableData() {
        List<User> users = context.getUserService().findAll();
        tableModel.setRowCount(0);
        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(), u.getUsername(), u.getFirstName() + " " + u.getLastName(),
                    u.getEmail(), u.getRole().getRoleName(), u.getStatus()
            });
        }
    }

    private void handleSearch() {
        String uname = txtSearchUsername.getText().trim();
        currentSelectedUser = context.getUserService().getUserByUsername(uname);
        if (currentSelectedUser != null) {
            txtUpdateFirst.setText(currentSelectedUser.getFirstName());
            txtUpdateLast.setText(currentSelectedUser.getLastName());
            txtUpdateEmail.setText(currentSelectedUser.getEmail());
            txtUpdatePhone.setText(currentSelectedUser.getPhoneNumber());
            cbStatus.setSelectedItem(currentSelectedUser.getStatus());
            // Đã xóa dòng cập nhật Role cho UI
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy người dùng!");
        }
    }

    private void handleUpdateInfo() {
        if (currentSelectedUser == null) return;
        UpdateStaffInfoDTO dto = new UpdateStaffInfoDTO(
                currentSelectedUser.getUsername(),
                txtUpdateFirst.getText(),
                txtUpdateLast.getText(),
                txtUpdateEmail.getText(),
                txtUpdatePhone.getText()
        );
        context.getUserService().updateUserInfo(dto);
        context.getUserService().updateStatusUser(currentSelectedUser.getUsername(), (UserStatus) cbStatus.getSelectedItem());
        JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin!");
        loadTableData();
    }

    private void handleCreateStaff() {
        CreateStaffDTO dto = new CreateStaffDTO(
                txtCreateFirst.getText(), txtCreateLast.getText(),
                txtCreateEmail.getText(), txtCreatePhone.getText(),
                txtCreateUser.getText(), new String(txtCreatePass.getPassword())
        );
        context.getAuthenticationService().createStaff(dto);
        JOptionPane.showMessageDialog(this, "Tạo nhân viên thành công!");
        loadTableData();
    }

    private void handleChangePass() {
        if (currentSelectedUser == null) return;
        String newP = new String(txtNewPassword.getPassword());
        context.getAuthenticationService().changePassword(currentSelectedUser.getUsername(), newP);
        JOptionPane.showMessageDialog(this, "Đã đổi mật khẩu!");
    }

    // --- UTILS ---
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(255, 248, 225));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);
    }

    private JTextField createRoundedTextField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(200, 35));
        f.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10), new EmptyBorder(0, 10, 0, 10)));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return f;
    }

    private void addFormField(JPanel p, String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(80, 80, 80));
        container.add(l, BorderLayout.NORTH);
        if (comp instanceof JPasswordField) {
            comp.setPreferredSize(new Dimension(200, 35));
            comp.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10), new EmptyBorder(0, 10, 0, 10)));
        }
        container.add(comp, BorderLayout.CENTER);
        p.add(container, gbc);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    class RoundedBorder implements javax.swing.border.Border {
        private int r;
        RoundedBorder(int r) { this.r = r; }
        public Insets getBorderInsets(Component c) { return new Insets(r/2, r/2, r/2, r/2); }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(220, 220, 220));
            g2.drawRoundRect(x, y, w - 1, h - 1, r, r);
            g2.dispose();
        }
    }
}