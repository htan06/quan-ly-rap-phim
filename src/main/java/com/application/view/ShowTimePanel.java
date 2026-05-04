package com.application.view;

import com.application.AppContext;
import com.application.dto.showtime.CreateShowTimeDTO;
import com.application.dto.showtime.UpdateShowTimeDTO;
import com.application.entity.ShowTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShowTimePanel extends JPanel {
    private final AppContext context;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private DefaultTableModel tableModel;
    private JTable table;

    // Components Tab Tra cứu
    private JTextField txtSearchMovieId = createRoundedTextField();
    private JTextField txtSearchDate = createRoundedTextField();

    // Components Tab Cập nhật
    private JTextField txtEditId = createRoundedTextField();
    private JTextField txtEditMovieId = createRoundedTextField(), txtEditRoomId = createRoundedTextField(),
            txtEditPrice = createRoundedTextField(), // Thêm giá vé
            txtEditStart = createRoundedTextField(), txtEditEnd = createRoundedTextField();

    // Components Tab Thêm mới
    private JTextField txtAddMovieId = createRoundedTextField(), txtAddRoomId = createRoundedTextField(),
            txtAddPrice = createRoundedTextField(), // Thêm giá vé
            txtAddStart = createRoundedTextField(), txtAddEnd = createRoundedTextField();

    public ShowTimePanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBackground(ThemeConfig.BACKGROUND);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("  Tra cứu lịch chiếu  ", createSearchTab());
        tabbedPane.addTab("  Cập nhật lịch chiếu  ", createUpdateTab());
        tabbedPane.addTab("  Thêm lịch chiếu mới  ", createAddTab());

        add(tabbedPane, BorderLayout.CENTER);
        loadAllData();
    }

    // --- TAB 1: TRA CỨU ---
    private JPanel createSearchTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ThemeConfig.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        searchBar.setBackground(Color.WHITE);
        searchBar.setBorder(new RoundedBorder(15));

        searchBar.add(new JLabel("Mã phim:"));
        searchBar.add(txtSearchMovieId);
        JButton btnSearchMovie = createStyledButton("Tìm theo Phim", ThemeConfig.PRIMARY);

        searchBar.add(new JLabel("Ngày (dd/MM/yyyy):"));
        txtSearchDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        searchBar.add(txtSearchDate);
        JButton btnSearchDate = createStyledButton("Tìm theo Ngày", new Color(108, 117, 125));

        searchBar.add(btnSearchMovie);
        searchBar.add(btnSearchDate);

        String[] columns = {"ID", "Mã phim", "Phòng", "Giá vé", "Bắt đầu", "Kết thúc"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        styleTable(table);

        btnSearchMovie.addActionListener(e -> handleSearchByMovie());
        btnSearchDate.addActionListener(e -> handleSearchByDate());

        panel.add(searchBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // --- TAB 2: CẬP NHẬT ---
    private JPanel createUpdateTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConfig.BACKGROUND);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(30, 40, 30, 40)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 10, 5); gbc.weightx = 1.0;

        addFormField(card, "Nhập ID lịch chiếu để tìm:", txtEditId, gbc, 0);
        JButton btnFind = createStyledButton("Tìm thông tin", ThemeConfig.PRIMARY);
        gbc.gridy = 1; card.add(btnFind, gbc);

        gbc.gridy = 2; gbc.insets = new Insets(15, 0, 15, 0); card.add(new JSeparator(), gbc);

        addFormField(card, "Mã phim:", txtEditMovieId, gbc, 3);
        addFormField(card, "Mã phòng:", txtEditRoomId, gbc, 4);
        addFormField(card, "Giá vé (VNĐ):", txtEditPrice, gbc, 5); // New field
        addFormField(card, "Bắt đầu (dd/MM/yyyy HH:mm):", txtEditStart, gbc, 6);
        addFormField(card, "Kết thúc (dd/MM/yyyy HH:mm):", txtEditEnd, gbc, 7);

        JButton btnUpdate = createStyledButton("Lưu thay đổi", new Color(40, 167, 69));
        gbc.gridy = 8; gbc.insets = new Insets(20, 5, 0, 5); card.add(btnUpdate, gbc);

        btnFind.addActionListener(e -> handleFindById());
        btnUpdate.addActionListener(e -> handleUpdate());

        panel.add(card);
        return panel;
    }

    // --- TAB 3: THÊM MỚI ---
    private JPanel createAddTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConfig.BACKGROUND);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(30, 40, 30, 40)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 10, 5); gbc.weightx = 1.0;

        addFormField(card, "Mã phim:", txtAddMovieId, gbc, 0);
        addFormField(card, "Mã phòng:", txtAddRoomId, gbc, 1);
        addFormField(card, "Giá vé (VNĐ):", txtAddPrice, gbc, 2); // New field
        addFormField(card, "Bắt đầu (dd/MM/yyyy HH:mm):", txtAddStart, gbc, 3);
        addFormField(card, "Kết thúc (dd/MM/yyyy HH:mm):", txtAddEnd, gbc, 4);

        JButton btnAdd = createStyledButton("Tạo lịch chiếu", ThemeConfig.PRIMARY);
        gbc.gridy = 5; gbc.insets = new Insets(25, 5, 0, 5); card.add(btnAdd, gbc);

        btnAdd.addActionListener(e -> handleAdd());

        panel.add(card);
        return panel;
    }

    // --- LOGIC XỬ LÝ ---

    private void handleSearchByMovie() {
        try {
            Long mid = Long.parseLong(txtSearchMovieId.getText().trim());
            renderTable(context.getShowTimeService().findByMovie(mid));
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Mã phim phải là số!"); }
    }

    private void handleSearchByDate() {
        try {
            LocalDate date = LocalDate.parse(txtSearchDate.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            renderTable(context.getShowTimeService().findByDate(date));
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Định dạng ngày sai (dd/MM/yyyy)!"); }
    }

    private void handleFindById() {
        try {
            Long id = Long.parseLong(txtEditId.getText().trim());
            ShowTime st = context.getShowTimeService().findById(id);
            if (st != null) {
                txtEditMovieId.setText(String.valueOf(st.getMovie().getId()));
                txtEditRoomId.setText(String.valueOf(st.getRoom().getId()));
                txtEditPrice.setText(st.getPrice().toString()); // Map price
                txtEditStart.setText(dateTimeFormat.format(st.getStartTime()));
                txtEditEnd.setText(dateTimeFormat.format(st.getEndTime()));
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Không tìm thấy lịch chiếu!"); }
    }

    private void handleUpdate() {
        try {
            UpdateShowTimeDTO dto = new UpdateShowTimeDTO(
                    Long.parseLong(txtEditId.getText()),
                    Long.parseLong(txtEditMovieId.getText()),
                    Integer.parseInt(txtEditRoomId.getText()),
                    new BigDecimal(txtEditPrice.getText().trim()), // Parse price
                    parseTimestamp(txtEditStart.getText()),
                    parseTimestamp(txtEditEnd.getText())
            );
            context.getShowTimeService().updateInfo(dto);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadAllData();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage()); }
    }

    private void handleAdd() {
        try {
            CreateShowTimeDTO dto = new CreateShowTimeDTO(
                    Long.parseLong(txtAddMovieId.getText()),
                    Integer.parseInt(txtAddRoomId.getText()),
                    new BigDecimal(txtAddPrice.getText().trim()), // Parse price
                    parseTimestamp(txtAddStart.getText()),
                    parseTimestamp(txtAddEnd.getText())
            );
            context.getShowTimeService().createShowTime(dto);
            JOptionPane.showMessageDialog(this, "Thêm lịch chiếu thành công!");
            loadAllData();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Lỗi dữ liệu: " + e.getMessage()); }
    }

    private Timestamp parseTimestamp(String str) throws Exception {
        return new Timestamp(dateTimeFormat.parse(str).getTime());
    }

    private void loadAllData() {
        renderTable(context.getShowTimeService().findAll());
    }

    private void renderTable(List<ShowTime> list) {
        tableModel.setRowCount(0);
        for (ShowTime st : list) {
            tableModel.addRow(new Object[]{
                    st.getId(),
                    st.getMovie().getId(),
                    st.getRoom().getName(),
                    String.format("%,.0f VNĐ", st.getPrice()), // Format hiển thị tiền tệ
                    dateTimeFormat.format(st.getStartTime()),
                    dateTimeFormat.format(st.getEndTime())
            });
        }
    }

    // --- UI HELPERS ---
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private JTextField createRoundedTextField() {
        JTextField f = new JTextField(12);
        f.setPreferredSize(new Dimension(0, 35));
        f.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10), new EmptyBorder(0, 10, 0, 10)));
        return f;
    }

    private void addFormField(JPanel p, String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridy = y;
        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        container.add(l, BorderLayout.NORTH);
        container.add(comp, BorderLayout.CENTER);
        p.add(container, gbc);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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