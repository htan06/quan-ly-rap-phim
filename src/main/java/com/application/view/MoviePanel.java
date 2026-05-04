package com.application.view;

import com.application.AppContext;
import com.application.dto.movie.CreateMovieDTO;
import com.application.dto.movie.UpdateMovieInfoDTO;
import com.application.entity.Movie;
import com.application.entity.enums.MovieStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MoviePanel extends JPanel {
    private final AppContext context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private DefaultTableModel tableModel;
    private JTable table;

    // Components cho Tab 2 (Quản lý)
    private JTextField txtSearchId = createRoundedTextField();
    private Movie currentSelectedMovie;
    private JTextField txtEditTitle = createRoundedTextField(), txtEditDirector = createRoundedTextField(),
            txtEditDuration = createRoundedTextField(), txtEditDesc = createRoundedTextField(),
            txtEditCast = createRoundedTextField(), txtEditGenre = createRoundedTextField(),
            txtEditLang = createRoundedTextField(), txtEditSub = createRoundedTextField(),
            txtEditCountry = createRoundedTextField(), txtEditAge = createRoundedTextField(),
            txtEditRel = createRoundedTextField(), txtEditEnd = createRoundedTextField(); // Trở về TextField

    private JComboBox<MovieStatus> cbEditStatus = new JComboBox<>(MovieStatus.values());

    // Components cho Tab 3 (Thêm phim mới)
    private JTextField txtAddTitle = createRoundedTextField(), txtAddDirector = createRoundedTextField(),
            txtAddDuration = createRoundedTextField(), txtAddDesc = createRoundedTextField(),
            txtAddCast = createRoundedTextField(), txtAddGenre = createRoundedTextField(),
            txtAddLang = createRoundedTextField(), txtAddSub = createRoundedTextField(),
            txtAddCountry = createRoundedTextField(), txtAddAge = createRoundedTextField(),
            txtAddRel = createRoundedTextField(), txtAddEnd = createRoundedTextField(); // Trở về TextField

    private JComboBox<MovieStatus> cbAddStatus = new JComboBox<>(MovieStatus.values());

    public MoviePanel(AppContext context) {
        this.context = context;
        dateFormat.setLenient(false); // Bắt lỗi nghiêm ngặt nếu ngày không tồn tại (vd: 31/02)

        setLayout(new BorderLayout());
        setBackground(ThemeConfig.BACKGROUND);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("  Danh sách phim  ", createListTab());
        tabbedPane.addTab("  Tìm kiếm & Cập nhật  ", createManageTab());
        tabbedPane.addTab("  Thêm phim mới  ", createAddMovieTab());

        add(tabbedPane, BorderLayout.CENTER);
        loadTableData();
    }

    private JPanel createListTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(ThemeConfig.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columns = {"ID", "Tên phim", "Đạo diễn", "Thời lượng", "Ngôn ngữ", "Trạng thái"};
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

    private JPanel createManageTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(ThemeConfig.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel searchBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchBox.setBackground(Color.WHITE);
        searchBox.setBorder(new RoundedBorder(15));
        searchBox.add(new JLabel("Mã phim (ID):"));
        searchBox.add(txtSearchId);
        JButton btnSearch = createStyledButton("Tìm kiếm", ThemeConfig.PRIMARY);
        btnSearch.addActionListener(e -> handleSearch());
        searchBox.add(btnSearch);

        JPanel formsGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        formsGrid.setBackground(ThemeConfig.BACKGROUND);
        formsGrid.add(createUpdateInfoForm());
        formsGrid.add(createUpdateStatusForm());

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

        addFormField(panel, "Tên phim:", txtEditTitle, gbc, 0);
        addFormField(panel, "Đạo diễn:", txtEditDirector, gbc, 1);
        addFormField(panel, "Thể loại:", txtEditGenre, gbc, 2);
        addFormField(panel, "Thời lượng:", txtEditDuration, gbc, 3);
        addFormField(panel, "Mô tả:", txtEditDesc, gbc, 4);
        addFormField(panel, "Diễn viên:", txtEditCast, gbc, 5);
        addFormField(panel, "Ngày khởi chiếu (dd/MM/yyyy):", txtEditRel, gbc, 6);
        addFormField(panel, "Ngày kết thúc (dd/MM/yyyy):", txtEditEnd, gbc, 7);
        addFormField(panel, "Ngôn ngữ:", txtEditLang, gbc, 8);
        addFormField(panel, "Phụ đề:", txtEditSub, gbc, 9);
        addFormField(panel, "Quốc gia:", txtEditCountry, gbc, 10);
        addFormField(panel, "Độ tuổi:", txtEditAge, gbc, 11);

        JButton btnUpdateInfo = createStyledButton("Lưu thông tin", ThemeConfig.PRIMARY);
        btnUpdateInfo.addActionListener(e -> handleUpdateInfo());
        gbc.gridy = 12; gbc.insets = new Insets(20, 5, 0, 5);
        panel.add(btnUpdateInfo, gbc);

        return panel;
    }

    private JPanel createUpdateStatusForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 5, 15, 5); gbc.weightx = 1.0;
        addFormField(panel, "Trạng thái phim:", cbEditStatus, gbc, 0);

        JButton btnUpdateStatus = createStyledButton("Cập nhật trạng thái", new Color(40, 167, 69));
        btnUpdateStatus.addActionListener(e -> handleUpdateStatus());
        gbc.gridy = 1; panel.add(btnUpdateStatus, gbc);

        gbc.gridy = 2; gbc.weighty = 1.0; panel.add(new JLabel(""), gbc);
        return panel;
    }

    private JScrollPane createAddMovieTab() {
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(ThemeConfig.BACKGROUND);
        container.setBorder(new EmptyBorder(20, 0, 20, 0));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(20), new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(5, 10, 10, 10); gbc.weightx = 1.0;

        addFormFieldToGrid(formCard, "Tên phim:", txtAddTitle, gbc, 0, 0);
        addFormFieldToGrid(formCard, "Đạo diễn:", txtAddDirector, gbc, 1, 0);
        addFormFieldToGrid(formCard, "Thể loại:", txtAddGenre, gbc, 0, 1);
        addFormFieldToGrid(formCard, "Thời lượng (phút):", txtAddDuration, gbc, 1, 1);
        addFormFieldToGrid(formCard, "Trạng thái ban đầu:", cbAddStatus, gbc, 0, 2);
        addFormFieldToGrid(formCard, "Ngôn ngữ:", txtAddLang, gbc, 1, 2);
        addFormFieldToGrid(formCard, "Ngày khởi chiếu (dd/MM/yyyy):", txtAddRel, gbc, 0, 3);
        addFormFieldToGrid(formCard, "Ngày kết thúc (dd/MM/yyyy):", txtAddEnd, gbc, 1, 3);
        addFormFieldToGrid(formCard, "Phụ đề:", txtAddSub, gbc, 0, 4);
        addFormFieldToGrid(formCard, "Quốc gia:", txtAddCountry, gbc, 1, 4);
        addFormFieldToGrid(formCard, "Độ tuổi:", txtAddAge, gbc, 0, 5);

        gbc.gridwidth = 2; gbc.gridx = 0;
        addFormFieldToGrid(formCard, "Mô tả phim:", txtAddDesc, gbc, 0, 6);
        addFormFieldToGrid(formCard, "Diễn viên:", txtAddCast, gbc, 0, 7);

        JButton btnSave = createStyledButton("Tạo phim mới", ThemeConfig.PRIMARY);
        btnSave.addActionListener(e -> handleAddMovie());
        gbc.gridy = 8; gbc.insets = new Insets(30, 10, 0, 10);
        formCard.add(btnSave, gbc);

        container.add(formCard);
        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ThemeConfig.BACKGROUND);
        return scroll;
    }

    // --- LOGIC ---
    private void handleAddMovie() {
        try {
            CreateMovieDTO dto = new CreateMovieDTO(
                    txtAddTitle.getText().trim(),
                    txtAddDesc.getText().trim(),
                    txtAddDirector.getText().trim(),
                    txtAddCast.getText().trim(),
                    txtAddGenre.getText().trim(),
                    Integer.parseInt(txtAddDuration.getText().trim()),
                    parseTimestamp(txtAddRel.getText().trim()),
                    parseTimestamp(txtAddEnd.getText().trim()),
                    txtAddLang.getText().trim(),
                    txtAddSub.getText().trim(),
                    txtAddCountry.getText().trim(),
                    txtAddAge.getText().trim(),
                    (MovieStatus) cbAddStatus.getSelectedItem()
            );
            context.getMovieService().createMovie(dto);
            JOptionPane.showMessageDialog(this, "Thêm phim mới thành công!");
            loadTableData();
            clearAddFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void handleUpdateInfo() {
        if (currentSelectedMovie == null) return;
        try {
            UpdateMovieInfoDTO dto = new UpdateMovieInfoDTO(
                    currentSelectedMovie.getId(),
                    txtEditTitle.getText().trim(),
                    txtEditDesc.getText().trim(),
                    txtEditDirector.getText().trim(),
                    txtEditCast.getText().trim(),
                    txtEditGenre.getText().trim(),
                    Integer.parseInt(txtEditDuration.getText().trim()),
                    parseTimestamp(txtEditRel.getText().trim()),
                    parseTimestamp(txtEditEnd.getText().trim()),
                    txtEditLang.getText().trim(),
                    txtEditSub.getText().trim(),
                    txtEditCountry.getText().trim(),
                    txtEditAge.getText().trim()
            );
            context.getMovieService().updateInfo(dto);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void handleSearch() {
        try {
            Long id = Long.parseLong(txtSearchId.getText().trim());
            currentSelectedMovie = context.getMovieService().findById(id);
            if (currentSelectedMovie != null) {
                txtEditTitle.setText(currentSelectedMovie.getTitle());
                txtEditDirector.setText(currentSelectedMovie.getDirector());
                txtEditGenre.setText(currentSelectedMovie.getGenre());
                txtEditDuration.setText(String.valueOf(currentSelectedMovie.getDuration()));
                txtEditDesc.setText(currentSelectedMovie.getDescription());
                txtEditCast.setText(currentSelectedMovie.getCast());
                txtEditLang.setText(currentSelectedMovie.getLanguage());
                txtEditSub.setText(currentSelectedMovie.getSubtitleLanguage());
                txtEditCountry.setText(currentSelectedMovie.getCountry());
                txtEditAge.setText(currentSelectedMovie.getAgeRating());
                cbEditStatus.setSelectedItem(currentSelectedMovie.getStatus());

                // Hiển thị ngày dạng string dd/MM/yyyy
                txtEditRel.setText(formatTimestamp(currentSelectedMovie.getReleaseDate()));
                txtEditEnd.setText(formatTimestamp(currentSelectedMovie.getEndDate()));
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy phim!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ID phải là số!");
        }
    }

    // --- DATE HELPERS ---
    private Timestamp parseTimestamp(String dateStr) throws Exception {
        try {
            Date parsedDate = dateFormat.parse(dateStr);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            throw new Exception("Ngày không hợp lệ. Vui lòng nhập đúng định dạng dd/MM/yyyy (VD: 01/01/2000)");
        }
    }

    private String formatTimestamp(Timestamp ts) {
        if (ts == null) return "";
        return dateFormat.format(new Date(ts.getTime()));
    }

    private void handleUpdateStatus() {
        if (currentSelectedMovie == null) return;
        context.getMovieService().updateStatus(currentSelectedMovie.getId(), (MovieStatus) cbEditStatus.getSelectedItem());
        JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái!");
        loadTableData();
    }

    private void loadTableData() {
        List<Movie> movies = context.getMovieService().findAll();
        tableModel.setRowCount(0);
        for (Movie m : movies) {
            tableModel.addRow(new Object[]{m.getId(), m.getTitle(), m.getDirector(), m.getDuration(), m.getLanguage(), m.getStatus()});
        }
    }

    private void clearAddFields() {
        txtAddTitle.setText(""); txtAddDirector.setText(""); txtAddDuration.setText("");
        txtAddDesc.setText(""); txtAddCast.setText(""); txtAddGenre.setText("");
        txtAddRel.setText(""); txtAddEnd.setText("");
        txtAddLang.setText(""); txtAddSub.setText("");
        txtAddCountry.setText(""); txtAddAge.setText("");
    }

    // --- UI HELPERS ---
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(255, 248, 225));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private JTextField createRoundedTextField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(200, 35));
        f.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(10), new EmptyBorder(0, 10, 0, 10)));
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
        container.add(comp, BorderLayout.CENTER);
        p.add(container, gbc);
    }

    private void addFormFieldToGrid(JPanel p, String label, JComponent comp, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x; gbc.gridy = y;
        gbc.gridwidth = (comp == txtAddDesc || comp == txtAddCast) ? 2 : 1;
        addFormField(p, label, comp, gbc, y);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent()) / 2 - 2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 40));
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