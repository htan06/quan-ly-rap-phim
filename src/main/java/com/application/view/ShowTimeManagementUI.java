package com.application.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

public class ShowTimeManagementUI extends JFrame implements ActionListener {

    private JTextField txtMovieName;
	private JComboBox<String> cbShowRoom;
	private JButton btnThem;
	private JButton btnXoa;
	private JButton btnLamMoi;
	private JButton btnLuu;
	private JTextField txtTimKiem;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane scroll;
	private JButton btnLogo;
	private JTextField txtShowTimeStart;
	private JTextField txtShowTimeEnd;

	public ShowTimeManagementUI() {
		initFrame();
		initUI();
		
	}
    
    private void initFrame() {
    	setTitle("Quản lý suất chiếu");
    	setSize(800, 580);
    	setLocationRelativeTo(null);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setResizable(false);
    }
    
    private void initUI() {
    	JPanel pnlMenu = new JPanel();
		pnlMenu.setPreferredSize(new Dimension(70, 500));
		pnlMenu.setBackground(Color.decode("#F4BC05"));
		pnlMenu.setLayout(new GridLayout(6,1,0,20));
		
		pnlMenu.add(createButton("../icon/home.png"));
		pnlMenu.add(createButton("../icon/film.png"));
		pnlMenu.add(createButton("../icon/showTime.png"));
		pnlMenu.add(createButton("../icon/staff.png"));
		pnlMenu.add(createButton("../icon/turnover.png"));
		pnlMenu.add(createButton("../icon/stonk.png"));
    	
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
		Box b7 = Box.createHorizontalBox();
		
		JLabel lblMovieName = new JLabel("Tên phim:");
		JLabel lblShowRoom = new JLabel("Phòng chiếu:");
		JLabel lblShowTimeStart = new JLabel("Giờ chiếu:");
		JLabel lblShowTimeEnd = new JLabel("Giờ kết thúc");
		
		txtMovieName = new JTextField(50); txtMovieName.setBorder(null);
		String[] rooms = {"Phòng 1", "Phòng 2", "Phòng 3", "Phòng 4", "Phòng 5"};
		cbShowRoom = new JComboBox<>(rooms);
		txtShowTimeStart = new JTextField(10); txtShowTimeStart.setBorder(null); txtShowTimeStart.setToolTipText("Nhập giờ chiếu theo định dạng HH:mm");
		txtShowTimeEnd = new JTextField(10); txtShowTimeEnd.setBorder(null); txtShowTimeEnd.setToolTipText("Nhập giờ chiếu theo định dạng HH:mm");
		
		btnThem = new JButton("Thêm");
		btnXoa = new JButton("Xoá");
		btnLamMoi = new JButton("Làm mới"); btnLamMoi.setToolTipText("Làm mới form");
		btnLuu = new JButton("Lưu");
		
		txtTimKiem = new JTextField(); txtTimKiem.setBorder(null); txtTimKiem.setToolTipText("Nhập tên phim");
		ImageIcon lgTimKiem = new ImageIcon(
				getClass().getResource("../icon/search.png")
				);
		Image TimKiemImg = lgTimKiem.getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		btnLogo = new JButton(new ImageIcon(TimKiemImg));
		
		String header[] = {"STT", "Tên phim", "Phòng chiếu", "Giờ chiếu", "Giờ kết thúc"};
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
		
		b1.add(lblMovieName); b1.add(Box.createHorizontalStrut(30)); b1.add(txtMovieName);
		b2.add(lblShowRoom); b2.add(Box.createHorizontalStrut(10)); b2.add(cbShowRoom);
		b3.add(lblShowTimeStart); b3.add(Box.createHorizontalStrut(30)); b3.add(txtShowTimeStart);
		b4.add(lblShowTimeEnd); b4.add(Box.createHorizontalStrut(15)); b4.add(txtShowTimeEnd);
		b5.add(btnThem); b5.add(Box.createHorizontalStrut(10)); 
		b5.add(btnXoa); b5.add(Box.createHorizontalStrut(10));
		b5.add(btnLamMoi); b5.add(Box.createHorizontalStrut(10)); 
		b5.add(btnLuu); 
		b6.add(txtTimKiem);b6.add(btnLogo);
		b7.add(scroll);
		
		
		bForm.add(Box.createVerticalStrut(20));
		bForm.add(b1); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b2); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b3); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b4); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b5); bForm.add(Box.createVerticalStrut(20));
		bForm.add(b6);
		bForm.add(b7);
		
		pnlInfo.add(bForm, BorderLayout.NORTH);
		add(pnlMenu, BorderLayout.WEST);
		add(pnlInfo, BorderLayout.CENTER);
		
		btnThem.addActionListener(this);
		btnXoa.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnLuu.addActionListener(this);
		btnLogo.addActionListener(this);
    }

	private JButton createButton(String iconPath) {
		JButton btn = new JButton();
		
		ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(img));
        
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(255, 255, 255, 100));
                btn.setOpaque(true);
            }

            public void mouseExited(MouseEvent evt) {
                btn.setOpaque(false);
            }
        });
        
		return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem)) {
			if(validInput()) {
				String movieName = txtMovieName.getText();
				String showRoom = (String) cbShowRoom.getSelectedItem();
				String showTimeStart = txtShowTimeStart.getText().trim();
				String showTimeEnd = txtShowTimeEnd.getText().trim();
				
				Object[] rowData = {model.getRowCount() + 1, movieName, showRoom, showTimeStart, showTimeEnd};
				model.addRow(rowData);
				JOptionPane.showMessageDialog(this, "Thêm suất chiếu thành công!");
			}
		} 
		
		else if (o.equals(btnXoa)) {
			deleteSelectedRow();
		} 
		
		else if (o.equals(btnLamMoi)) {
			txtMovieName.setText("");
			cbShowRoom.setSelectedIndex(0);
			txtShowTimeStart.setText("");
			txtShowTimeEnd.setText("");
			
		} 
		
		else if (o.equals(btnLogo)) {
			findShowTimesByMovieName();
			// Hiển thị thông báo nếu không tìm thấy suất chiếu nào
			if (table.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy suất chiếu nào với tên phim đã nhập!");
			}
		}
		
		else if (o.equals(btnLuu)) {
			
		}
	}

	private void findShowTimesByMovieName() {
		String searchTerm = txtTimKiem.getText().trim().toLowerCase();
		DefaultTableModel searchModel = new DefaultTableModel(new String[] {"STT", "Tên phim", "Phòng chiếu", "Giờ chiếu", "Giờ kết thúc"}, 0);
		for (int i = 0; i < model.getRowCount(); i++) {
			String movieName = model.getValueAt(i, 1).toString().toLowerCase();
			if (movieName.contains(searchTerm)) {
				searchModel.addRow(new Object[] {
						model.getValueAt(i, 0),
						model.getValueAt(i, 1),
						model.getValueAt(i, 2),
						model.getValueAt(i, 3),
						model.getValueAt(i, 4)
				});
			}
		}
		table.setModel(searchModel);
		// Cập nhật lại cột STT sau khi tìm kiếm
	    for (int i = 0; i < searchModel.getRowCount(); i++) {
	        searchModel.setValueAt(i + 1, i, 0);
	    }
	}

	private void deleteSelectedRow() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá suất chiếu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				model.removeRow(selectedRow);
				JOptionPane.showMessageDialog(this, "Xoá suất chiếu thành công!");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một suất chiếu để xoá!");
		}
	}

	private boolean validInput() {
		String movieName = txtMovieName.getText().trim();
		String showTimeStart = txtShowTimeStart.getText().trim();
		String showTimeEnd = txtShowTimeEnd.getText().trim();
		
		if (movieName.isEmpty() || showTimeStart.isEmpty() || showTimeEnd.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
			return false;
		}
		
		if (!showTimeStart.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			JOptionPane.showMessageDialog(this, "Giờ chiếu không hợp lệ! Vui lòng nhập theo định dạng HH:mm.");
			txtShowTimeStart.requestFocus();
			return false;
		}
		
		if (!showTimeEnd.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			JOptionPane.showMessageDialog(this, "Giờ chiếu không hợp lệ! Vui lòng nhập theo định dạng HH:mm.");
			txtShowTimeEnd.requestFocus();
			return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		new ShowTimeManagementUI().setVisible(true);
	}
}
