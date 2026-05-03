package com.application.view;

import com.application.entity.ShowTime;
import com.application.service.ShowTimeService;
import com.application.view.controller.BookingController;
import com.application.view.controller.NavigationController;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ShowTimeUI extends JFrame implements ActionListener {

    private final ShowTimeService showTimeService;
    private final NavigationController navigationController;
    private final BookingController bookingController;
    private final Long movieId;
    private final boolean isBookingFlow;

    private Long selectedShowTimeId;

    // Admin form fields
    private JTextField txtMovieName, txtShowTimeStart, txtShowTimeEnd;
    private JComboBox<String> cbShowRoom;
    private JButton btnThem, btnXoa, btnLamMoi, btnLuu, btnLogo;
    private JTextField txtTimKiem;
    private DefaultTableModel model;
    private JTable table;

    // Admin constructor
    public ShowTimeUI(ShowTimeService showTimeService, NavigationController navigationController) {
        this.showTimeService = showTimeService;
        this.navigationController = navigationController;
        this.bookingController = null;
        this.movieId = null;
        this.isBookingFlow = false;
        initFrame();
        initAdminUI();
        loadAllShowTimes();
    }

    // Booking constructor
    public ShowTimeUI(Long movieId, ShowTimeService showTimeService,
                      NavigationController navigationController, BookingController bookingController) {
        this.movieId = movieId;
        this.showTimeService = showTimeService;
        this.navigationController = navigationController;
        this.bookingController = bookingController;
        this.isBookingFlow = true;
        initFrame();
        initBookingUI();
        loadShowTimesForMovie();
    }

    private void initFrame() {
        setTitle("Quản lý suất chiếu");
        setSize(800, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    // ── ADMIN UI ────────────────────────────────────────────────────────────
    private void initAdminUI() {
        add(buildSidebar(), BorderLayout.WEST);

        JPanel pnlInfo = new JPanel();
        pnlInfo.setBackground(Color.decode("#E7E6E6"));

        Box bForm = Box.createVerticalBox();

        txtMovieName    = new JTextField(30); txtMovieName.setBorder(null);
        txtShowTimeStart = new JTextField(10); txtShowTimeStart.setBorder(null);
        txtShowTimeEnd   = new JTextField(10); txtShowTimeEnd.setBorder(null);
        String[] rooms = {"Phòng 1","Phòng 2","Phòng 3","Phòng 4","Phòng 5"};
        cbShowRoom = new JComboBox<>(rooms);

        btnThem   = new JButton("Thêm");
        btnXoa    = new JButton("Xoá");
        btnLamMoi = new JButton("Làm mới");
        btnLuu    = new JButton("Lưu");
        txtTimKiem = new JTextField(20); txtTimKiem.setBorder(null);
        btnLogo   = new JButton("🔍");

        String[] header = {"STT","Tên phim","Phòng chiếu","Giờ chiếu","Giờ kết thúc"};
        model = new DefaultTableModel(header, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(638, 250));
        scroll.setBorder(BorderFactory.createEmptyBorder());

        bForm.add(Box.createVerticalStrut(15));
        bForm.add(row("Tên phim:", txtMovieName));           bForm.add(Box.createVerticalStrut(10));
        bForm.add(row("Phòng chiếu:", cbShowRoom));          bForm.add(Box.createVerticalStrut(10));
        bForm.add(row("Giờ chiếu:", txtShowTimeStart));      bForm.add(Box.createVerticalStrut(10));
        bForm.add(row("Giờ kết thúc:", txtShowTimeEnd));     bForm.add(Box.createVerticalStrut(10));

        Box btnRow = Box.createHorizontalBox();
        for (JButton b : new JButton[]{btnThem, btnXoa, btnLamMoi, btnLuu}) {
            btnRow.add(b); btnRow.add(Box.createHorizontalStrut(8));
        }
        bForm.add(btnRow); bForm.add(Box.createVerticalStrut(10));

        Box searchRow = Box.createHorizontalBox();
        searchRow.add(txtTimKiem); searchRow.add(btnLogo);
        bForm.add(searchRow); bForm.add(Box.createVerticalStrut(5));
        bForm.add(scroll);

        pnlInfo.add(bForm);
        add(pnlInfo, BorderLayout.CENTER);

        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnLuu.addActionListener(this);
        btnLogo.addActionListener(this);
    }

    private void loadAllShowTimes() {
        model.setRowCount(0);
        try {
            List<ShowTime> list = showTimeService.findAll();
            int i = 1;
            for (ShowTime st : list) {
                model.addRow(new Object[]{
                        i++,
                        st.getMovie().getTitle(),
                        st.getRoom().getName(),
                        st.getStartTime(),
                        st.getEndTime()
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ── BOOKING UI ──────────────────────────────────────────────────────────
    private void initBookingUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.decode("#E7E6E6"));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Chọn suất chiếu (double-click để chọn)");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"ID","Phòng","Giờ bắt đầu","Giờ kết thúc"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                selectedShowTimeId = (Long) model.getValueAt(table.getSelectedRow(), 0);
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0)
                    confirmSelection();
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.decode("#E7E6E6"));
        JButton btnBack = new JButton("← Quay lại");
        JButton btnOk   = new JButton("Chọn");
        btnBack.addActionListener(e -> bookingController.goBackToMovies());
        btnOk.addActionListener(e -> confirmSelection());
        btnPanel.add(btnBack); btnPanel.add(btnOk);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadShowTimesForMovie() {
        model.setRowCount(0);
        try {
            List<ShowTime> list = showTimeService.findAll();
            for (ShowTime st : list) {
                if (st.getMovie().getId().equals(movieId)) {
                    model.addRow(new Object[]{
                            st.getId(),
                            st.getRoom().getName(),
                            st.getStartTime(),
                            st.getEndTime()
                    });
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void confirmSelection() {
        if (selectedShowTimeId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một suất chiếu!");
            return;
        }
        bookingController.selectShowTime(selectedShowTimeId);
        navigationController.navigateToSeatSelection(selectedShowTimeId);
    }

    // ── ADMIN ACTIONS ───────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnThem) {
            if (validInput()) {
                model.addRow(new Object[]{
                        model.getRowCount() + 1,
                        txtMovieName.getText().trim(),
                        cbShowRoom.getSelectedItem(),
                        txtShowTimeStart.getText().trim(),
                        txtShowTimeEnd.getText().trim()
                });
                JOptionPane.showMessageDialog(this, "Thêm suất chiếu thành công!");
            }
        } else if (src == btnXoa) {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn một suất chiếu!"); return; }
            if (JOptionPane.showConfirmDialog(this, "Xác nhận xoá?", "Xoá", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                model.removeRow(row);
                JOptionPane.showMessageDialog(this, "Xoá thành công!");
            }
        } else if (src == btnLamMoi) {
            txtMovieName.setText(""); cbShowRoom.setSelectedIndex(0);
            txtShowTimeStart.setText(""); txtShowTimeEnd.setText("");
        } else if (src == btnLuu) {
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
        } else if (src == btnLogo) {
            searchByName();
        }
    }

    private void searchByName() {
        String term = txtTimKiem.getText().trim().toLowerCase();
        DefaultTableModel m2 = new DefaultTableModel(
                new String[]{"STT","Tên phim","Phòng chiếu","Giờ chiếu","Giờ kết thúc"}, 0);
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).toString().toLowerCase().contains(term))
                m2.addRow(new Object[]{m2.getRowCount()+1,
                        model.getValueAt(i,1), model.getValueAt(i,2),
                        model.getValueAt(i,3), model.getValueAt(i,4)});
        }
        table.setModel(m2);
        if (m2.getRowCount() == 0)
            JOptionPane.showMessageDialog(this, "Không tìm thấy suất chiếu nào!");
    }

    private boolean validInput() {
        String name  = txtMovieName.getText().trim();
        String start = txtShowTimeStart.getText().trim();
        String end   = txtShowTimeEnd.getText().trim();
        if (name.isEmpty() || start.isEmpty() || end.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!"); return false; }
        if (!start.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            JOptionPane.showMessageDialog(this, "Giờ bắt đầu không hợp lệ (HH:mm)");
            txtShowTimeStart.requestFocus(); return false; }
        if (!end.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
            JOptionPane.showMessageDialog(this, "Giờ kết thúc không hợp lệ (HH:mm)");
            txtShowTimeEnd.requestFocus(); return false; }
        return true;
    }

    // ── HELPERS ─────────────────────────────────────────────────────────────
    private Box row(String label, JComponent field) {
        Box b = Box.createHorizontalBox();
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(100, 25));
        b.add(lbl); b.add(Box.createHorizontalStrut(10)); b.add(field);
        return b;
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
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            p.add(btn);
        }
        return p;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(24);
        t.setShowHorizontalLines(false);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.getTableHeader().setBackground(new Color(240, 240, 240));
        t.getTableHeader().setForeground(Color.BLACK);
        t.getFont(); // ensure painted
    }
}