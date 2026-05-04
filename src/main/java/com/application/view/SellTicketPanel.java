package com.application.view;

import com.application.AppContext;
import com.application.dto.booking.CreateBookingDTO;
import com.application.entity.Movie;
import com.application.entity.ShowTime;
import com.application.entity.Seat;
import com.application.entity.enums.BookingStatus;
import com.application.entity.enums.MovieStatus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SellTicketPanel extends JPanel {
    private final AppContext context;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);
    private int step = 0;

    // Models & Components
    private final DefaultListModel<Movie> movieListModel = new DefaultListModel<>();
    private final JList<Movie> movieJList = new JList<>(movieListModel);

    private final DefaultListModel<ShowTime> showTimeListModel = new DefaultListModel<>();
    private final JList<ShowTime> showTimeJList = new JList<>(showTimeListModel);

    private final JPanel seatGridPanel = new JPanel();
    private final List<Long> selectedSeatIds = new ArrayList<>();

    private final JButton btnBack = createStyledButton("← Quay lại", Color.GRAY);
    private final JButton btnNext = createStyledButton("Tiếp tục →", ThemeConfig.PRIMARY);
    private final JButton btnConfirm = createStyledButton("Xác nhận", ThemeConfig.PRIMARY);

    public SellTicketPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout(20, 20));
        setBackground(ThemeConfig.BACKGROUND);
        setBorder(new EmptyBorder(20, 40, 20, 40));

        content.setBackground(ThemeConfig.BACKGROUND);
        content.add(createMovieStep(), "STEP1");
        content.add(createShowtimeStep(), "STEP2");
        content.add(createSeatStep(), "STEP3");
        content.add(createConfirmStep(), "STEP4");

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        footer.setBackground(ThemeConfig.BACKGROUND);
        footer.add(btnBack);
        footer.add(btnNext);
        footer.add(btnConfirm);

        btnBack.setEnabled(false);
        btnConfirm.setVisible(false);

        btnBack.addActionListener(e -> prevStep());
        btnNext.addActionListener(e -> nextStep());
        btnConfirm.addActionListener(e -> confirmBooking());

        add(content, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        loadMoviesData();
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void loadMoviesData() {
        List<Movie> showingMovies = context.getMovieService().findByStatus(MovieStatus.NOW_SHOWING);
        movieListModel.clear();
        for (Movie m : showingMovies) movieListModel.addElement(m);
    }

    private void loadShowTimesData(Long movieId) {
        List<ShowTime> showTimes = context.getShowTimeService().findByMovie(movieId);
        showTimeListModel.clear();
        for (ShowTime st : showTimes) showTimeListModel.addElement(st);
    }

    private void loadSeatsData(ShowTime st) {
        seatGridPanel.removeAll();
        selectedSeatIds.clear();

        List<Seat> seats = context.getSeatService().findByRoomId(st.getRoom().getId().intValue());
        List<Long> bookedIds = context.getBookingService().seatBookedId(st.getId());

        seatGridPanel.setLayout(new GridLayout(0, 10, 5, 5)); // 10 ghế mỗi hàng

        for (Seat seat : seats) {
            JButton btnSeat = new JButton(seat.getName());
            btnSeat.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnSeat.setFocusPainted(false);

            if (bookedIds.contains(seat.getId())) {
                btnSeat.setBackground(Color.LIGHT_GRAY);
                btnSeat.setEnabled(false);
            } else {
                btnSeat.setBackground(Color.WHITE);
                btnSeat.addActionListener(e -> {
                    if (btnSeat.getBackground() == Color.WHITE) {
                        btnSeat.setBackground(ThemeConfig.PRIMARY);
                        selectedSeatIds.add(seat.getId());
                    } else {
                        btnSeat.setBackground(Color.WHITE);
                        selectedSeatIds.remove(seat.getId());
                    }
                });
            }
            seatGridPanel.add(btnSeat);
        }
        seatGridPanel.revalidate();
        seatGridPanel.repaint();
    }

    private JPanel createMovieStep() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ThemeConfig.BACKGROUND);
        JLabel lbl = new JLabel("1. Chọn phim đang chiếu");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        movieJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Movie) {
                    Movie m = (Movie) value;
                    setText(m.getTitle() + "    [" + m.getDuration() + " min]   " + "Đạo diễn:" + m.getDirector());
                    setBorder(new EmptyBorder(8, 10, 8, 10));
                }
                return this;
            }
        });
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(movieJList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createShowtimeStep() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ThemeConfig.BACKGROUND);
        JLabel lbl = new JLabel("2. Chọn suất chiếu");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        showTimeJList.setCellRenderer(new DefaultListCellRenderer() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ShowTime) {
                    ShowTime st = (ShowTime) value;
                    setText(sdf.format(st.getStartTime()) + "   - Phòng: " + st.getRoom().getName());
                    setBorder(new EmptyBorder(8, 10, 8, 10));
                }
                return this;
            }
        });
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(showTimeJList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSeatStep() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(ThemeConfig.BACKGROUND);
        JLabel lbl = new JLabel("3. Chọn ghế", JLabel.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        seatGridPanel.setBackground(ThemeConfig.BACKGROUND);
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(seatGridPanel), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createConfirmStep() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeConfig.BACKGROUND);
        JLabel summary = new JLabel("<html><center><b>XÁC NHẬN</b><br>Vui lòng nhấn xác nhận để xuất hóa đơn</center></html>");
        summary.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(summary);
        return panel;
    }

    private void nextStep() {
        if (step == 0) {
            Movie selected = movieJList.getSelectedValue();
            if (selected == null) { JOptionPane.showMessageDialog(this, "Hãy chọn một bộ phim!"); return; }
            loadShowTimesData(selected.getId());
        } else if (step == 1) {
            ShowTime selected = showTimeJList.getSelectedValue();
            if (selected == null) { JOptionPane.showMessageDialog(this, "Hãy chọn suất chiếu!"); return; }
            loadSeatsData(selected);
        } else if (step == 2) {
            if (selectedSeatIds.isEmpty()) { JOptionPane.showMessageDialog(this, "Hãy chọn ít nhất một ghế!"); return; }
        }

        step++;
        if (step >= 3) { btnNext.setVisible(false); btnConfirm.setVisible(true); }
        btnBack.setEnabled(true);
        cardLayout.next(content);
    }

    private void prevStep() {
        step--;
        if (step <= 0) btnBack.setEnabled(false);
        btnNext.setVisible(true);
        btnConfirm.setVisible(false);
        cardLayout.previous(content);
    }

    private void confirmBooking() {
        try {
            // 1. Thu thập dữ liệu
            ShowTime selectedST = showTimeJList.getSelectedValue();
            Long staffId = context.getSessionService().getCurrent().getUserId(); // Lấy ID nhân viên đang đăng nhập

            CreateBookingDTO dto = new CreateBookingDTO(
                    selectedST.getId(),
                    null, // Membership (nếu có)
                    staffId,
                    selectedSeatIds
            );

            // 2. Tạo Booking trong Database
            Long bookingId = context.getBookingService().create(dto);

            // Tự động chuyển trạng thái sang PAID (hoặc tùy logic của bạn)
            context.getBookingService().updateStatus(bookingId, BookingStatus.PAID);

            // 3. Xuất hóa đơn ra file
            String invoiceFolder = "D:/invoices";
            Files.createDirectories(Paths.get(invoiceFolder)); // Tạo thư mục nếu chưa có
            context.getInvoiceService().exportInvoice(bookingId, invoiceFolder);

            // 4. Hiển thị hóa đơn lên màn hình
            showInvoiceDialog(bookingId, invoiceFolder);

            // 5. Reset flow
            resetFlow();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xử lý đặt vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInvoiceDialog(Long bookingId, String folder) {
        try {
            String filePath = folder + "/invoice_" + bookingId + ".txt";
            String content = Files.readString(Paths.get(filePath));

            JTextArea textArea = new JTextArea(content);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            textArea.setMargin(new EmptyBorder(10, 10, 10, 10).getBorderInsets());

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 500));

            JOptionPane.showMessageDialog(this, scrollPane, "HÓA ĐƠN THANH TOÁN", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể hiển thị hóa đơn!");
        }
    }

    private void resetFlow() {
        step = 0;
        selectedSeatIds.clear();
        movieJList.clearSelection();
        showTimeListModel.clear();
        seatGridPanel.removeAll();
        cardLayout.show(content, "STEP1");

        btnBack.setEnabled(false);
        btnNext.setVisible(true);
        btnConfirm.setVisible(false);
        loadMoviesData();
    }
}