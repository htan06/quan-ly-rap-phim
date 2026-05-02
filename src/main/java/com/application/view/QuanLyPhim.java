package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class QuanLyPhim extends JFrame{
	private JTextField txtTenPhim;
	private JTextField txtTheLoai;
	private JTextField txtThoiLuong;
	private JDateChooser dateChooser;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnLamMoi;
	private JButton btnLuu;
	private JTextField txtTimKiem;
	private ImageIcon icon;
	private DefaultTableModel model;
	public QuanLyPhim() {
		init();
	}
	public void init() {	
		setTitle("Danh Sach Phim");
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		add(menu(), BorderLayout.WEST);
		add(info(), BorderLayout.EAST);
	}
	public JPanel menu() {
		JPanel pnlMenu = new JPanel();
		pnlMenu.setPreferredSize(new Dimension(50, 500));
		pnlMenu.setBackground(Color.decode("#F4BC05"));
		return pnlMenu;
	}
	public JPanel info() {
		JPanel pnlInfo = new JPanel();
		pnlInfo.setPreferredSize(new Dimension(750, 500));
		pnlInfo.setBackground(Color.decode("#E7E6E6"));
		UIManager.put("Label.font", new Font("Time New Roman", Font.PLAIN, 16));
		
		
		Box bForm = Box.createVerticalBox();
		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();
		Box b4 = Box.createHorizontalBox();
		Box b5 = Box.createHorizontalBox();
		
		JLabel lblTenPhim = new JLabel("Tên phim:"); 
		JLabel lblTheLoai = new JLabel("Thể loại:"); 
		JLabel lblThoiLuong = new JLabel("Thời lượng:"); 
		JLabel lblNgayRaMat = new JLabel("Ngày ra mắt:");
		
		
		txtTenPhim = new JTextField(50); txtTenPhim.setBorder(null);
		txtTheLoai = new JTextField(); txtTheLoai.setBorder(null);
		txtThoiLuong = new JTextField(); txtThoiLuong.setBorder(null);
		dateChooser = new JDateChooser(); dateChooser.setBorder(null);
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setDate(new Date());
		dateChooser.setPreferredSize(new Dimension(100, 20));
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		btnThem = new JButton("Thêm");
		btnXoa = new JButton("Xoá");
		btnLamMoi = new JButton("Làm mới"); btnLamMoi.setToolTipText("Làm mới form");
		btnLuu = new JButton("Lưu");
		
		txtTimKiem = new JTextField(); txtTimKiem.setBorder(null);
		ImageIcon lgTimKiem = new ImageIcon(
			    getClass().getResource("/icon/search.png")
			);
		Image img = lgTimKiem.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		JButton btnLogo = new JButton(new ImageIcon(img));
		
		String header[] = {"STT", "Tên phim", "Thể loại", "Thời lượng", "Ngày ra mắt"};
		model = new DefaultTableModel(header, 0);
		model.addRow(new Object[] {"1", "mua do", "hanh dong", "180p", "sadad"});
		JTable table = new JTable(model); 
		table.setBorder(null); 
		table.getTableHeader().setBorder(null);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.getTableHeader().setBackground(new Color(240, 240,240));
		table.getTableHeader().setOpaque(true);
		table.getTableHeader().setForeground(Color.BLACK);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value,
		            boolean isSelected, boolean hasFocus, int row, int column) {
		        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        c.setBackground(Color.white);
		        c.setForeground(Color.BLACK);
		        if (c instanceof JComponent) {
		            ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		        }
		        return c;
		    }
		});
		
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
		});
		
		JScrollPane sc = new JScrollPane(table);
	
		sc.setBorder(BorderFactory.createEmptyBorder());
		sc.setViewportBorder(BorderFactory.createEmptyBorder());
		sc.setPreferredSize(new Dimension(638, 290));
		sc.getViewport().setOpaque(true);
		sc.getViewport().setBackground(Color.white);
		
		
		b1.add(lblTenPhim); b1.add(Box.createHorizontalStrut(20)); b1.add(txtTenPhim);
		b2.add(lblTheLoai); b2.add(Box.createHorizontalStrut(28)); b2.add(txtTheLoai);
		b2.add(Box.createHorizontalStrut(3));
		b2.add(lblThoiLuong); b2.add(Box.createHorizontalStrut(10)); b2.add(txtThoiLuong);
		b3.add(lblNgayRaMat); b3.add(Box.createHorizontalStrut(4)); b3.add(dateChooser);
		b4.add(btnThem); b4.add(Box.createHorizontalStrut(30));
		b4.add(btnXoa); b4.add(Box.createHorizontalStrut(30));
		b4.add(btnLamMoi); b4.add(Box.createHorizontalStrut(30));
		b4.add(btnLuu);
		b5.add(txtTimKiem); b5.add(btnLogo);
		bForm.add(Box.createVerticalStrut(10));
		bForm.add(b1); bForm.add(Box.createVerticalStrut(10));
		bForm.add(b2); bForm.add(Box.createVerticalStrut(10));
		bForm.add(b3); bForm.add(Box.createVerticalStrut(10));
		bForm.add(b4); bForm.add(Box.createVerticalStrut(10));
		bForm.add(b5); 
		pnlInfo.add(bForm, BorderLayout.NORTH);
		pnlInfo.add(sc, BorderLayout.CENTER);
		return pnlInfo;
	}
	public static void main(String[] args) {
		new QuanLyPhim().setVisible(true);
	}
}
