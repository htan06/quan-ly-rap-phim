package com.application.view;

import com.application.entity.Genre;
import com.application.entity.Movie;
import com.application.service.MovieService;
import com.application.view.controller.BookingController;
import com.application.view.controller.NavigationController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class ListMovieUI extends JFrame {

    private final MovieService movieService;
    private final NavigationController navigationController;
    private final BookingController bookingController; // null if admin flow
    private final boolean isBookingFlow;

    private DefaultTableModel model;
    private JTable table;

    // Admin flow
    public ListMovieUI(MovieService movieService, NavigationController navigationController) {
        this.movieService = movieService;
        this.navigationController = navigationController;
        this.bookingController = null;
        this.isBookingFlow = false;
        initFrame();
        initUI();
    }

    // Booking flow
    public ListMovieUI(MovieService movieService,
                       NavigationController navigationController,
                       BookingController bookingController) {
        this.movieService = movieService;
        this.navigationController = navigationController;
        this.bookingController = bookingController;
        this.isBookingFlow = true;
        initFrame();
        initUI();
    }

    private void initFrame() {
        setTitle(isBookingFlow ? "Chọn phim" : "Quản lý phim");
        setSize(800, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initUI() {
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new GridLayout(6, 1, 0, 20));
        sidebar.setPreferredSize(new Dimension(70, 500));
        sidebar.setBackground(Color.decode("#F4BC05"));
        String[] icons = {"home","film","showTime","staff","turnover","stonk"};
        for (String icon : icons) sidebar.add(makeSidebarBtn(icon));
        return sidebar;
    }

    private JButton makeSidebarBtn(String name) {
        JButton btn = new JButton(name.substring(0,1).toUpperCase());
        btn.setFont(new Font("Arial", Font.BOLD, 10));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(Color.decode("#F4BC05"));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(Color.WHITE); btn.setForeground(Color.decode("#F4BC05")); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(Color.decode("#F4BC05")); btn.setForeground(Color.WHITE); }
        });
        return btn;
    }

    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.decode("#E7E6E6"));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel(isBookingFlow ? "Chọn phim (double-click để chọn)" : "Danh sách phim");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Tên phim", "Thể loại", "Thời lượng"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getTableHeader().setBackground(new Color(240, 240, 240));

        if (isBookingFlow) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                        Long movieId = (Long) model.getValueAt(table.getSelectedRow(), 0);
                        bookingController.selectMovie(movieId);
                    }
                }
            });
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        if (!isBookingFlow) {
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            btnPanel.setBackground(Color.decode("#E7E6E6"));
            JButton btnLogout = new JButton("Đăng xuất");
            btnLogout.addActionListener(e -> navigationController.logout());
            btnPanel.add(btnLogout);
            panel.add(btnPanel, BorderLayout.SOUTH);
        } else {
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            btnPanel.setBackground(Color.decode("#E7E6E6"));
            JButton btnLogout = new JButton("Đăng xuất");
            btnLogout.addActionListener(e -> navigationController.logout());
            btnPanel.add(btnLogout);
            panel.add(btnPanel, BorderLayout.SOUTH);
        }

        loadMovies();
        return panel;
    }

    private void loadMovies() {
        model.setRowCount(0);
        try {
            List<Movie> movies = movieService.findAll();
            for (Movie m : movies) {
                model.addRow(new Object[]{
                        m.getId(),
                        m.getTitle(),
                        m.getGenres().stream().map(Genre::getName).collect(Collectors.joining()),
                        m.getDuration() + " phút"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}