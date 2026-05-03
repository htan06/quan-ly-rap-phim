package com.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class QuanLyPhim extends JFrame implements ActionListener {

	private JTextField txtTenPhim;
	private JTextField txtTheLoai;
	private JTextField txtThoiLuong;
	private JDateChooser dateChooser;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnLamMoi;
	private JButton btnLuu;
	private JTextField txtTimKiem;
	private DefaultTableModel model;
	private JTable table;

	// Lưu dữ liệu gốc để filter/reset không mất hàng
	private final List<Object[]> allData = new ArrayList<>();
	// STT tự tăng
	private int sttCounter = 1;
	// Hàng đang được chọn để sửa/xoá (-1 = chưa chọn)
	private int selectedRow = -1;

	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

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
		JPanel pnlInfo = new JPanel(new BorderLayout());
		pnlInfo.setPreferredSize(new Dimension(730, 500));
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

		txtTenPhim = new JTextField(50);
		txtTenPhim.setBorder(null);
		txtTheLoai = new JTextField();
		txtTheLoai.setBorder(null);
		txtThoiLuong = new JTextField();
		txtThoiLuong.setBorder(null);

		dateChooser = new JDateChooser();
		dateChooser.setBorder(null);
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.setDate(new Date());
		dateChooser.setPreferredSize(new Dimension(100, 20));
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

		btnThem = new JButton("Thêm");
		btnThem.setActionCommand("THEM");
		btnThem.addActionListener(this);
		btnXoa = new JButton("Xoá");
		btnXoa.setActionCommand("XOA");
		btnXoa.addActionListener(this);
		btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setActionCommand("LAM_MOI");
		btnLamMoi.addActionListener(this);
		btnLuu = new JButton("Lưu");
		btnLuu.setActionCommand("LUU");
		btnLuu.addActionListener(this);

		txtTimKiem = new JTextField();
		txtTimKiem.setBorder(null);
		txtTimKiem.setActionCommand("SEARCH");
		txtTimKiem.addActionListener(this);
		ImageIcon lgTimKiem = new ImageIcon(
				getClass().getResource("/icon/search.png"));
		Image img = lgTimKiem.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		JButton btnLogo = new JButton(new ImageIcon(img));
		btnLogo.setActionCommand("SEARCH");
		btnLogo.addActionListener(this);

		// --- Bảng ---
		String[] header = { "STT", "Tên phim", "Thể loại", "Thời lượng", "Ngày ra mắt" };
		model = new DefaultTableModel(header, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		// Dữ liệu mẫu
		addRowToData(new Object[] { "1", "Mưa đỏ", "Hành động", "180 phút", "01/01/2024" });

		table = new JTable(model);
		table.setBorder(null);
		table.getTableHeader().setBorder(null);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setBackground(new Color(240, 240, 240));
		table.getTableHeader().setOpaque(true);
		table.getTableHeader().setForeground(Color.BLACK);

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(isSelected ? new Color(200, 220, 255) : Color.WHITE);
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
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				label.setBackground(new Color(240, 240, 240));
				label.setForeground(Color.BLACK);
				label.setOpaque(true);
				label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				return label;
			}
		});

		// Click hàng → điền form
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int viewRow = table.getSelectedRow();
					if (viewRow >= 0) {
						selectedRow = viewRow;
						txtTenPhim.setText((String) model.getValueAt(viewRow, 1));
						txtTheLoai.setText((String) model.getValueAt(viewRow, 2));
						txtThoiLuong.setText((String) model.getValueAt(viewRow, 3));
						try {
							Date d = SDF.parse((String) model.getValueAt(viewRow, 4));
							dateChooser.setDate(d);
						} catch (Exception ex) {
							dateChooser.setDate(new Date());
						}
					}
				}
			}
		});

		JScrollPane sc = new JScrollPane(table);
		sc.setBorder(BorderFactory.createEmptyBorder());
		sc.setViewportBorder(BorderFactory.createEmptyBorder());
		sc.setPreferredSize(new Dimension(638, 290));
		sc.getViewport().setOpaque(true);
		sc.getViewport().setBackground(Color.WHITE);

		b1.add(lblTenPhim);
		b1.add(Box.createHorizontalStrut(20));
		b1.add(txtTenPhim);
		b2.add(lblTheLoai);
		b2.add(Box.createHorizontalStrut(28));
		b2.add(txtTheLoai);
		b2.add(Box.createHorizontalStrut(3));
		b2.add(lblThoiLuong);
		b2.add(Box.createHorizontalStrut(10));
		b2.add(txtThoiLuong);
		b3.add(lblNgayRaMat);
		b3.add(Box.createHorizontalStrut(4));
		b3.add(dateChooser);
		b4.add(btnThem);
		b4.add(Box.createHorizontalStrut(30));
		b4.add(btnXoa);
		b4.add(Box.createHorizontalStrut(30));
		b4.add(btnLamMoi);
		b4.add(Box.createHorizontalStrut(30));
		b4.add(btnLuu);
		b5.add(txtTimKiem);
		b5.add(btnLogo);

		bForm.add(Box.createVerticalStrut(10));
		bForm.add(b1);
		bForm.add(Box.createVerticalStrut(10));
		bForm.add(b2);
		bForm.add(Box.createVerticalStrut(10));
		bForm.add(b3);
		bForm.add(Box.createVerticalStrut(10));
		bForm.add(b4);
		bForm.add(Box.createVerticalStrut(10));
		bForm.add(b5);

		pnlInfo.add(bForm, BorderLayout.NORTH);
		pnlInfo.add(sc, BorderLayout.CENTER);
		return pnlInfo;
	}

	// ---------------------------------------------------------------
	// Helper: thêm hàng vào allData VÀ model
	// ---------------------------------------------------------------
	private void addRowToData(Object[] row) {
		allData.add(row);
		model.addRow(row);
	}

	// ---------------------------------------------------------------
	// Validation
	// ---------------------------------------------------------------
	private boolean validateForm() {
		if (txtTenPhim.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phim!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtTenPhim.requestFocus();
			return false;
		}
		if (txtTheLoai.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập thể loại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtTheLoai.requestFocus();
			return false;
		}
		if (txtThoiLuong.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập thời lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtThoiLuong.requestFocus();
			return false;
		}
		if (dateChooser.getDate() == null) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày ra mắt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void resetForm() {
		txtTenPhim.setText("");
		txtTheLoai.setText("");
		txtThoiLuong.setText("");
		dateChooser.setDate(new Date());
		txtTimKiem.setText("");
		table.clearSelection();
		selectedRow = -1;
	}

	private void reloadTable() {
		model.setRowCount(0);
		for (Object[] row : allData)
			model.addRow(row);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("THEM".equals(cmd)) {
			if (!validateForm())
				return;
			String ngay = SDF.format(dateChooser.getDate());
			Object[] newRow = {
					String.valueOf(sttCounter++),
					txtTenPhim.getText().trim(),
					txtTheLoai.getText().trim(),
					txtThoiLuong.getText().trim(),
					ngay
			};
			addRowToData(newRow);
			resetForm();
			JOptionPane.showMessageDialog(this, "Thêm phim thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}

		// --- Xoá ---
		else if ("XOA".equals(cmd)) {
			if (selectedRow < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần xoá!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có chắc muốn xoá phim \"" + model.getValueAt(selectedRow, 1) + "\"?",
					"Xác nhận xoá", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				// Xác định row trong allData theo STT
				String sttToDelete = (String) model.getValueAt(selectedRow, 0);
				allData.removeIf(r -> r[0].equals(sttToDelete));
				reloadTable();
				resetForm();
				JOptionPane.showMessageDialog(this, "Xoá phim thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		// --- Lưu (cập nhật) ---
		else if ("LUU".equals(cmd)) {
			if (selectedRow < 0) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần cập nhật!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!validateForm())
				return;
			String sttEdit = (String) model.getValueAt(selectedRow, 0);
			String ngay = SDF.format(dateChooser.getDate());
			Object[] updated = {
					sttEdit,
					txtTenPhim.getText().trim(),
					txtTheLoai.getText().trim(),
					txtThoiLuong.getText().trim(),
					ngay
			};
			// Cập nhật trong allData
			for (int i = 0; i < allData.size(); i++) {
				if (allData.get(i)[0].equals(sttEdit)) {
					allData.set(i, updated);
					break;
				}
			}
			reloadTable();
			resetForm();
			JOptionPane.showMessageDialog(this, "Cập nhật phim thành công!", "Thông báo",
					JOptionPane.INFORMATION_MESSAGE);
		}

		// --- Làm mới ---
		else if ("LAM_MOI".equals(cmd)) {
			reloadTable();
			resetForm();
		}

		// --- Tìm kiếm ---
		else if ("SEARCH".equals(cmd)) {
			String keyword = txtTimKiem.getText().trim().toLowerCase();
			model.setRowCount(0);
			for (Object[] row : allData) {
				if (keyword.isEmpty() || row[1].toString().toLowerCase().contains(keyword)) {
					model.addRow(row);
				}
			}
		}
	}

	public static void main(String[] args) {
		new QuanLyPhim().setVisible(true);
	}
}
