package com.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class BookingManagementUI extends JFrame {
	
	private JComboBox<String> cbShowTimeId;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnLamMoi;
	private JButton btnLuu;
	private JTextField txtTimKiem;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane scroll;
	private JDateChooser dateChooser;
	private JButton btnLogo;
	private JComboBox cbStaffId;
	private JComboBox cbStatus;
	private JTextField txtTax;
	private JTextField txtTotalPrice;

	public BookingManagementUI() {
		initFrame();
		initUI();
		
	}
    
    private void initFrame() {
    	setTitle("Quản lý đặt vé");
    	setSize(800, 580);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setResizable(false);
    }
    
    private void initUI() {
    	JPanel pnlMenu = new JPanel();
		pnlMenu.setPreferredSize(new Dimension(70, 500));
		pnlMenu.setBackground(Color.decode("#F4BC05"));
    	
		JPanel pnlInfo = new JPanel();
		pnlInfo.setPreferredSize(new Dimension(730, 500));
		pnlInfo.setBackground(Color.decode("#E7E6E6"));
		UIManager.put("Label.font", new Font("Time New Roman", Font.PLAIN, 16));
		
		Box bForm = Box.createVerticalBox();
		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();
		Box b4 = Box.createHorizontalBox();
		Box b5 = Box.createHorizontalBox();
		Box b6 = Box.createHorizontalBox();
		
		JLabel lblShowTimeId = new JLabel("Mã suất chiếu:");
		JLabel lblStaffId = new JLabel("Mã nhân viên:");
		JLabel lblStatus = new JLabel("Trạng thái:");
		JLabel lblTotalTax = new JLabel("Thuế:");
		JLabel lblTotalPrice = new JLabel("Tổng tiền:");
		JLabel lblCreatedAt = new JLabel("Ngày tạo:");
		
		cbShowTimeId = new JComboBox<>();
		//viết đẩy dữ liệu từ db lên cb vô đây
		
		
		
		cbStaffId = new JComboBox<>();
		// viết đẩy dữ liệu từ db lên cb vô đây
		
		
		
		String[] status = {"","đã huỷ","đang xử lý","đã thanh toán","hết hạn"};
		cbStatus = new JComboBox<>(status);
		txtTax = new JTextField(20);
		txtTotalPrice = new JTextField(20);
		
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setDate(new Date());
		dateChooser.setPreferredSize(new Dimension(100, 20));
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor(); 
		editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		btnThem = new JButton("Thêm");
		btnXoa = new JButton("Xoá");
		btnLamMoi = new JButton("Làm mới"); btnLamMoi.setToolTipText("Làm mới form");
		btnLuu = new JButton("Lưu");
		
		txtTimKiem = new JTextField(); txtTimKiem.setBorder(null); txtTimKiem.setToolTipText("Nhập tên phim");
		ImageIcon lgTimKiem = new ImageIcon(
				getClass().getResource("../icon/search.png")
				);
		Image img = lgTimKiem.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		btnLogo = new JButton(new ImageIcon(img));
		
		String header[] = {"STT", "Mã suất chiếu", "Mã nhân viên", "Trạng thái", "Thuế", "Tổng tiền", "Ngày tạo"};
		model = new DefaultTableModel(header, 0);
		table = new JTable(model);
		table.setBorder(null); 
		table.getTableHeader().setBorder(null);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0)); // Loại bỏ khoảng cách giữa các ô
		table.getTableHeader().setBackground(new Color(240, 240,240));
		table.getTableHeader().setOpaque(true);
		table.getTableHeader().setForeground(Color.BLACK);

		table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		            boolean isSelected, boolean hasFocus, int row, int column) {
		        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        label.setBackground(new Color(240,240,240));
		        label.setForeground(Color.BLACK);
		        label.setOpaque(true);
		        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		        return label;
		    }
		}); // Tùy chỉnh renderer cho header để thêm padding
		
		scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setViewportBorder(BorderFactory.createEmptyBorder());
		scroll.setPreferredSize(new Dimension(638, 290));
		scroll.getViewport().setOpaque(true);
		scroll.getViewport().setBackground(Color.white);
		
		b1.add(lblShowTimeId); b1.add(Box.createHorizontalStrut(10)); b1.add(cbShowTimeId);
		b2.add(lblStaffId); b2.add(Box.createHorizontalStrut(10)); b2.add(cbStaffId);
		b2.add(lblStatus); b2.add(Box.createHorizontalStrut(10)); b2.add(cbStatus);
		b3.add(lblTotalTax); b3.add(Box.createHorizontalStrut(10)); b3.add(txtTax);
		b3.add(lblTotalPrice); b3.add(Box.createHorizontalStrut(10)); b3.add(txtTotalPrice);
		b3.add(lblCreatedAt); b3.add(Box.createHorizontalStrut(10)); b3.add(dateChooser);
		b4.add(btnThem); b4.add(Box.createHorizontalStrut(10)); 
		b4.add(btnXoa); b4.add(Box.createHorizontalStrut(10));
		b4.add(btnLamMoi); b4.add(Box.createHorizontalStrut(10)); 
		b4.add(btnLuu);
		b5.add(txtTimKiem);b5.add(btnLogo);
		b6.add(scroll);
		
		
		bForm.add(Box.createVerticalStrut(20));
		bForm.add(b1); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b2); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b3); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b4); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b5);  
		bForm.add(b6);
		
		pnlInfo.add(bForm, BorderLayout.NORTH);
		add(pnlMenu, BorderLayout.WEST);
		add(pnlInfo, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) {
		new BookingManagementUI().setVisible(true);
	}
    
}