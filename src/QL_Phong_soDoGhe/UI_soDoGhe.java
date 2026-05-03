package QL_Phong_soDoGhe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class UI_soDoGhe extends JFrame{

	    private final Color COLOR_BG = new Color(15, 15, 15);          // Nền ứng dụng
	    private final Color COLOR_CARD = new Color(25, 25, 25);        // Nền khu vực ghế
	    
	   
	    private final Color SEAT_EMPTY_BG = new Color(45, 40, 20);
	    private final Color SEAT_EMPTY_FG = new Color(255, 193, 7);
	    
	   
	    private final Color SEAT_SELECTED_BG = new Color(255, 160, 0); 
	    private final Color SEAT_SELECTED_FG = new Color(10, 10, 10);
	    
	   
	    private final Color SEAT_OCCUPIED_BG = new Color(60, 60, 60);
	    private final Color SEAT_OCCUPIED_FG = new Color(130, 130, 130);

	   
	    enum SeatStatus { AVAILABLE, SELECTING, OCCUPIED }
	    private Map<String, SeatStatus> seatData = new LinkedHashMap<>();
	    private Map<String, SeatButton> buttonMap = new LinkedHashMap<>();

	    public UI_soDoGhe() {
	        initData();
	        initUI();
	    }

	    private void initData() {
	        String[] rows = {"F", "E", "D", "C", "B", "A"};
	        for (String r : rows) {
	            for (int i = 1; i <= 12; i++) {
	                seatData.put(r + i, SeatStatus.AVAILABLE);
	            }
	        }
	        // Giả lập một vài ghế đã bán
	        seatData.put("D5", SeatStatus.OCCUPIED);
	        seatData.put("D6", SeatStatus.OCCUPIED);
	    }

	    private void initUI() {
	        setTitle("Quản Lý Đặt Ghế Cinema - SoftEng Year 2");
	        setSize(1100, 800);
	        setResizable(false);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        getContentPane().setBackground(COLOR_BG);
	        setLayout(new BorderLayout(20, 20));

	        // Header
	        JLabel title = new JLabel("HÃY CHỌN GHẾ CỦA BẠN", SwingConstants.CENTER);
	        title.setForeground(Color.WHITE);
	        title.setFont(new Font("Inter", Font.BOLD, 28));
	        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
	        add(title, BorderLayout.NORTH);

	        // Grid Ghế
	        JPanel mainContainer = new JPanel(new GridBagLayout());
	        mainContainer.setOpaque(false);
	        
	        JPanel seatGrid = new JPanel(new GridLayout(6, 12, 12, 12));
	        seatGrid.setBackground(COLOR_CARD);
	        seatGrid.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

	        for (String id : seatData.keySet()) {
	            SeatButton btn = new SeatButton(id);
	            btn.addActionListener(e -> toggleSeat(id));
	            buttonMap.put(id, btn);
	            seatGrid.add(btn);
	        }
	        
	        mainContainer.add(seatGrid);
	        add(mainContainer, BorderLayout.CENTER);

	        // Footer: Nút bấm đặt vé
	        JButton btnConfirm = new JButton("ĐẶT GHẾ");
	        btnConfirm.setFocusPainted(false);
	        btnConfirm.setFont(new Font("SansSerif", Font.BOLD, 18));
	        btnConfirm.setBackground(SEAT_SELECTED_BG);
	        btnConfirm.setForeground(SEAT_SELECTED_FG);
	        btnConfirm.setPreferredSize(new Dimension(200, 50));
	        btnConfirm.addActionListener(e -> confirmAction());

	        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 40));
	        footer.setOpaque(false);
	        footer.add(btnConfirm);
	        add(footer, BorderLayout.SOUTH);

	        refreshUI();
	        setLocationRelativeTo(null);
	    }

	    private void toggleSeat(String id) {
	        SeatStatus current = seatData.get(id);
	        if (current == SeatStatus.AVAILABLE) seatData.put(id, SeatStatus.SELECTING);
	        else if (current == SeatStatus.SELECTING) seatData.put(id, SeatStatus.AVAILABLE);
	        refreshUI();
	    }

	    private void refreshUI() {
	        for (String id : seatData.keySet()) {
	            SeatStatus status = seatData.get(id);
	            SeatButton btn = buttonMap.get(id);
	            if (status == SeatStatus.AVAILABLE) btn.setStyle(SEAT_EMPTY_BG, SEAT_EMPTY_FG, true);
	            else if (status == SeatStatus.SELECTING) btn.setStyle(SEAT_SELECTED_BG, SEAT_SELECTED_FG, false);
	            else if (status == SeatStatus.OCCUPIED) {
	                btn.setStyle(SEAT_OCCUPIED_BG, SEAT_OCCUPIED_FG, false);
	                btn.setEnabled(false);
	            }
	        }
	    }

	    private void confirmAction() {
	        long count = seatData.values().stream().filter(s -> s == SeatStatus.SELECTING).count();
	        if (count > 0) {
	            JOptionPane.showMessageDialog(this, "Đã xác nhận đặt " + count + " ghế thành công!");
	            seatData.forEach((id, status) -> {
	                if (status == SeatStatus.SELECTING) seatData.put(id, SeatStatus.OCCUPIED);
	            });
	            refreshUI();
	        } else {
	            JOptionPane.showMessageDialog(this, "Vui lòng chọn ghế trước!");
	        }
	    }

	    private class SeatButton extends JButton {
	        private Color bgColor;
	        private boolean hasBorder;

	        public SeatButton(String text) {
	            super(text);
	            setContentAreaFilled(false);
	            setFocusPainted(false);
	            setBorderPainted(false);
	            setCursor(new Cursor(Cursor.HAND_CURSOR));
	            setFont(new Font("SansSerif", Font.BOLD, 11));
	        }

	        public void setStyle(Color bg, Color fg, boolean border) {
	            this.bgColor = bg;
	            this.setForeground(fg);
	            this.hasBorder = border;
	            repaint();
	        }

	        @Override
	        protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g.create();
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            
	            // Vẽ nền bo tròn
	            g2.setColor(bgColor);
	            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
	            
	            // Vẽ viền nếu cần
	           // if (hasBorder) {
	           //     g2.setColor(getForeground().deriveColor(0, 1, 1, 0.3f)); 
	           //     g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
	           // }

	            super.paintComponent(g2);
	            g2.dispose();
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new UI_soDoGhe().setVisible(true));
	    }
	}
