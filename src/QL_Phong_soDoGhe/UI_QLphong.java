package QL_Phong_soDoGhe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI_QLphong extends JFrame {

	    private JTextField txtTenPhim, txtNgayChieu, txtGioChieu, txtChoNgoi, txtGia, txtSearch;
	    private JComboBox<String> cbPhongChieu;
	    private JTable table;
	    private DefaultTableModel tableModel;
	    private int autoIncrementSTT = 1;

	    public UI_QLphong() {
	        initUI();
	        handleEvents();
	    }

	    private void initUI() {
	        setTitle("Quản Lý Phim - Cinema Management");
	        setSize(1000, 700);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout());

	        JPanel sidebar = new JPanel(new GridLayout(6, 1, 0, 0));
	        sidebar.setBackground(new Color(241, 196, 15)); 
	        sidebar.setPreferredSize(new Dimension(80, 0));

	        String[] menuIcons = {"🏠", "🎬", "📅", "👥", "📊", "📈"};
	        for (int i = 0; i < menuIcons.length; i++) {
	            JButton btn = new JButton(menuIcons[i]);
	            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
	            btn.setBorderPainted(false);
	            btn.setFocusPainted(false);
	            
	            if (i == 2) {
	                btn.setBackground(Color.WHITE);
	            } else {
	                btn.setBackground(new Color(241, 196, 15));
	            }
	            sidebar.add(btn);
	        }

	       
	        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
	        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
	        mainPanel.setBackground(new Color(236, 240, 241));

	      
	        JPanel inputPanel = new JPanel(new GridBagLayout());
	        inputPanel.setOpaque(false);
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(8, 8, 8, 8);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        txtTenPhim = new JTextField();
	        cbPhongChieu = new JComboBox<>(new String[]{"Phòng 01", "Phòng 02", "Phòng 03"});
	        txtNgayChieu = new JTextField("mm/dd/yyyy");
	        txtGioChieu = new JTextField();
	        txtChoNgoi = new JTextField();
	        txtGia = new JTextField();

	   
	        addComp(inputPanel, new JLabel("Tên phim:"), 0, 0, 1, gbc);
	        addComp(inputPanel, txtTenPhim, 1, 0, 20, gbc);

	        addComp(inputPanel, new JLabel("Phòng chiếu:"), 0, 1, 1, gbc);
	        addComp(inputPanel, cbPhongChieu, 1, 1, 2, gbc);
	        addComp(inputPanel, new JLabel("Ngày chiếu:"), 3, 1, 1, gbc);
	        addComp(inputPanel, txtNgayChieu, 4, 1, 3, gbc);

	        addComp(inputPanel, new JLabel("Giờ chiếu:"), 0, 2, 1, gbc);
	        addComp(inputPanel, txtGioChieu, 1, 2, 2, gbc);
	        addComp(inputPanel, new JLabel("Chỗ ngồi:"), 3, 2, 1, gbc);
	        addComp(inputPanel, txtChoNgoi, 4, 2, 1, gbc);
	        addComp(inputPanel, new JLabel("Giá:"), 5, 2, 1, gbc);
	        addComp(inputPanel, txtGia, 6, 2, 1, gbc);

	        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
	        btnPanel.setOpaque(false);
	        JButton btnThem = createBtn("Thêm", new Color(52, 152, 219));
	        JButton btnXoa = createBtn("Xoá", new Color(231, 76, 60));
	        JButton btnLamMoi = createBtn("Làm mới", new Color(46, 204, 113));
	        JButton btnLuu = createBtn("Lưu", new Color(243, 156, 18));
	        btnPanel.add(btnThem); btnPanel.add(btnXoa); btnPanel.add(btnLamMoi); btnPanel.add(btnLuu);

	        txtSearch = new JTextField("Search...");
	        String[] cols = {"STT", "Tên phim", "Phòng chiếu", "Ngày chiếu", "Giờ chiếu", "Chỗ ngồi", "Giá"};
	        tableModel = new DefaultTableModel(cols, 0);
	        table = new JTable(tableModel);
	        JScrollPane scrollPane = new JScrollPane(table);

	        JPanel topContainer = new JPanel(new BorderLayout());
	        topContainer.setOpaque(false);
	        topContainer.add(inputPanel, BorderLayout.NORTH);
	        topContainer.add(btnPanel, BorderLayout.CENTER);
	        topContainer.add(txtSearch, BorderLayout.SOUTH);

	        mainPanel.add(topContainer, BorderLayout.NORTH);
	        mainPanel.add(scrollPane, BorderLayout.CENTER);

	        add(sidebar, BorderLayout.WEST);
	        add(mainPanel, BorderLayout.CENTER);
	    }

	  
	    private void addComp(JPanel p, JComponent c, int x, int y, int w, GridBagConstraints gbc) {
	        gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = w;
	        p.add(c, gbc);
	    }

	
	    private JButton createBtn(String text, Color bg) {
	        JButton btn = new JButton(text);
	        btn.setBackground(bg);
	        btn.setForeground(Color.WHITE);
	        btn.setFocusPainted(false);
	        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
	        btn.setPreferredSize(new Dimension(120, 35));
	        return btn;
	    }

	  
	    private void handleEvents() {
	        
	        JPanel top = (JPanel) ((JPanel) getContentPane().getComponent(1)).getComponent(0);
	        JPanel btnP = (JPanel) top.getComponent(1);
	        JButton btnThem = (JButton) btnP.getComponent(0);
	        JButton btnXoa = (JButton) btnP.getComponent(1);
	        JButton btnLamMoi = (JButton) btnP.getComponent(2);

	        
	        btnThem.addActionListener(e -> {
	            if (txtTenPhim.getText().isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phim!");
	                return;
	            }
	            Object[] row = {
	                autoIncrementSTT++,
	                txtTenPhim.getText(),
	                cbPhongChieu.getSelectedItem(),
	                txtNgayChieu.getText(),
	                txtGioChieu.getText(),
	                txtChoNgoi.getText(),
	                txtGia.getText()
	            };
	            tableModel.addRow(row);
	        });

	     
	        btnXoa.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            if (row != -1) {
	                tableModel.removeRow(row);
	            } else {
	                JOptionPane.showMessageDialog(this, "Chọn một dòng để xoá!");
	            }
	        });

	        btnLamMoi.addActionListener(e -> {
	            txtTenPhim.setText("");
	            txtNgayChieu.setText("mm/dd/yyyy");
	            txtGioChieu.setText("");
	            txtChoNgoi.setText("");
	            txtGia.setText("");
	            cbPhongChieu.setSelectedIndex(0);
	        });

	        table.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	                int row = table.getSelectedRow();
	                txtTenPhim.setText(tableModel.getValueAt(row, 1).toString());
	                cbPhongChieu.setSelectedItem(tableModel.getValueAt(row, 2).toString());
	                txtNgayChieu.setText(tableModel.getValueAt(row, 3).toString());
	                txtGioChieu.setText(tableModel.getValueAt(row, 4).toString());
	                txtChoNgoi.setText(tableModel.getValueAt(row, 5).toString());
	                txtGia.setText(tableModel.getValueAt(row, 6).toString());
	            }
	        });
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new UI_QLphong().setVisible(true));
	    }
	}
   