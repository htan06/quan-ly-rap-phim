package com.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeDoanhThu extends JFrame implements ActionListener {
	private JButton btnLuu;
	private JButton btnLamMoi;
	private JTextField txtTimKiem;
	private DefaultTableModel model;
	// Lưu dữ liệu gốc để filter/reset không mất hàng
	private final List<Object[]> allData = new ArrayList<>();

	public ThongKeDoanhThu() {
		init();
	}

	public void init() {
		setTitle("Thống kê doanh thu");
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

	JPanel createCard(String title, String value, String percent) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(Color.white);
		card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		JLabel lblTitle = new JLabel(title);
		lblTitle.setForeground(Color.GRAY);

		JLabel lblValue = new JLabel(value);
		lblValue.setFont(new Font("Arial", Font.BOLD, 18));

		JLabel lblPercent = new JLabel(percent);
		lblPercent.setForeground(new Color(0, 150, 0));

		card.add(lblTitle);
		card.add(Box.createVerticalStrut(5));
		card.add(lblValue);
		card.add(Box.createVerticalStrut(5));
		card.add(lblPercent);

		return card;
	}

	@SuppressWarnings("deprecation")
	public JScrollPane tableTk() {
		String header[] = { "Tên phim", "Vé bán", "Doanh thu vé", "Tổng" };
		model = new DefaultTableModel(header, 0);

		allData.add(new Object[] { "Mua Đỏ", 120, 1200000L, 2000000L });
		allData.add(new Object[] { "Interstellar", 85, 2550000L, 4000000L });
		allData.add(new Object[] { "The Batman", 150, 4500000L, 7000000L });
		for (Object[] row : allData)
			model.addRow(row);

		JTable table = new JTable(model);
		table.setBorder(null);
		table.getTableHeader().setBorder(null);
		table.setShowHorizontalLines(false);
		table.setShowVerticalLines(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.getTableHeader().setBackground(new Color(240, 240, 240));
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
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				label.setBackground(new Color(240, 240, 240));
				label.setForeground(Color.BLACK);
				label.setOpaque(true);
				label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				return label;
			}
		});
		NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
		DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
			@Override
			protected void setValue(Object value) {
				if (value instanceof Number) {
					setText(vndFormat.format(value));
				} else if (value instanceof String) {
					try {
						long parsed = Long.parseLong((String) value);
						setText(vndFormat.format(parsed));
					} catch (NumberFormatException e) {
						setText((String) value);
					}
				} else {
					setText("");
				}

			}
		};

		table.getColumnModel().getColumn(2).setCellRenderer(moneyRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(moneyRenderer);

		JScrollPane sc = new JScrollPane(table);

		sc.setBorder(BorderFactory.createEmptyBorder());
		sc.setViewportBorder(BorderFactory.createEmptyBorder());
		sc.setPreferredSize(new Dimension(638, 290));
		sc.getViewport().setOpaque(true);
		sc.getViewport().setBackground(Color.white);
		return sc;
	}

	public JPanel info() {
		JPanel pnlInfo = new JPanel();
		pnlInfo.setPreferredSize(new Dimension(750, 500));
		pnlInfo.setBackground(Color.decode("#E7E6E6"));

		JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		pnlHeader.add(Box.createHorizontalStrut(400));
		pnlHeader.setBackground(Color.decode("#E7E6E6"));
		btnLuu = new JButton("Lưu");
		btnLuu.addActionListener(this);

		btnLamMoi = new JButton("Làm mới");
		btnLamMoi.addActionListener(this);

		String[] month = { "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
				"Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9",
				"Tháng 10", "Tháng 11", "Tháng 12" };
		JComboBox<String> cbMonth = new JComboBox<String>(month);
		cbMonth.setActionCommand("FILTER_MONTH");
		cbMonth.addActionListener(this);

		pnlHeader.add(btnLuu);
		pnlHeader.add(cbMonth);
		pnlHeader.add(btnLamMoi);

		JPanel pnlMain = new JPanel(new GridLayout(1, 3, 50, 10));
		pnlMain.setBackground(Color.decode("#E7E6E6"));

		pnlMain.add(createCard("Tổng doanh thu", "56.540.000 đ", "+12% so với năm ngoái"));
		pnlMain.add(createCard("Doanh thu bán vé", "35.540.000 đ", "+8.3% so với năm ngoái"));
		pnlMain.add(createCard("Doanh thu bắp nước", "21.000.000 đ", "+5% so với năm ngoái"));

		Box bTimKiem = Box.createHorizontalBox();
		txtTimKiem = new JTextField(50);
		txtTimKiem.setBorder(null);
		txtTimKiem.setActionCommand("SEARCH");
		txtTimKiem.addActionListener(this);

		ImageIcon lgTimKiem = new ImageIcon(
				getClass().getResource("../icon/search.png"));
		Image img = lgTimKiem.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		JButton btnLogo = new JButton(new ImageIcon(img));
		btnLogo.setActionCommand("SEARCH");
		btnLogo.addActionListener(this);

		bTimKiem.add(txtTimKiem);
		bTimKiem.add(btnLogo);

		pnlInfo.add(pnlHeader, BorderLayout.NORTH);
		pnlInfo.add(pnlMain, BorderLayout.CENTER);
		pnlInfo.add(bTimKiem);
		pnlInfo.add(tableTk());
		return pnlInfo;
	}

	public static void main(String[] args) {
		new ThongKeDoanhThu().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if (e.getSource() == btnLuu) {

			System.out.println("[Lưu] Tổng số hàng: " + model.getRowCount());
		}

		else if (e.getSource() == btnLamMoi) {
			txtTimKiem.setText("");
			model.setRowCount(0);
			for (Object[] row : allData) {
				model.addRow(row);
			}
		}

		else if ("FILTER_MONTH".equals(cmd)) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) e.getSource();
			int thang = cb.getSelectedIndex() + 1;
			txtTimKiem.setText("");

			model.setRowCount(0);
			for (Object[] row : allData) {
				model.addRow(row);
			}
			System.out.println("[Lọc] Tháng " + thang);
		}

		else if ("SEARCH".equals(cmd)) {
			String keyword = txtTimKiem.getText().trim().toLowerCase();
			model.setRowCount(0);
			for (Object[] row : allData) {
				if (keyword.isEmpty() || row[0].toString().toLowerCase().contains(keyword)) {
					model.addRow(row);
				}
			}
		}
	}
}
