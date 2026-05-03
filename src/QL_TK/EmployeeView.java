package QL_TK;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeView extends JPanel {
   
    private JTextField txtName, txtPos, txtPhone, txtSearch;
    private JComboBox<String> cbStatus;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnDel, btnClear, btnSave;

    public EmployeeView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 230, 230)); 
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

     
        initComponents();
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);

        setupActions();
    }

    private void initComponents() {
        txtName = new JTextField();
        txtPos = new JTextField();
        txtPhone = new JTextField();
        txtSearch = new JTextField("Search");
        txtSearch.setForeground(Color.GRAY);
        cbStatus = new JComboBox<>(new String[]{"Đang làm việc", "Nghỉ phép"});

        
        btnAdd = createStyledBtn("Thêm", new Color(64, 169, 255));   
        btnDel = createStyledBtn("Xoá", new Color(255, 77, 79));     
        btnClear = createStyledBtn("Làm mới", new Color(82, 196, 26)); 
        btnSave = createStyledBtn("Lưu", new Color(255, 197, 61));    

        String[] cols = {"STT", "Tên nhân viên", "Vị trí", "Số điện thoại", "Trạng thái"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(30);
    }

    private JPanel createTopPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        
        g.gridx = 0; g.gridy = 0; p.add(new JLabel("Tên nhân viên:"), g);
        g.gridx = 1; g.gridwidth = 3; p.add(txtName, g);

        
        g.gridx = 0; g.gridy = 1; g.gridwidth = 1; p.add(new JLabel("Vị trí:"), g);
        g.gridx = 1; g.gridwidth = 3; p.add(txtPos, g);

       
        g.gridx = 0; g.gridy = 2; g.gridwidth = 1; p.add(new JLabel("Số điện thoại:"), g);
        g.gridx = 1; g.weightx = 0.5; p.add(txtPhone, g);
        g.gridx = 2; g.weightx = 0; p.add(new JLabel("   Trạng thái:"), g);
        g.gridx = 3; g.weightx = 0.5; p.add(cbStatus, g);

        
        JPanel btnBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnBox.setOpaque(false);
        btnBox.add(btnAdd); btnBox.add(btnDel); btnBox.add(btnClear); btnBox.add(btnSave);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 4; p.add(btnBox, g);

        return p;
    }

    private JPanel createCenterPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);

       
        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.add(txtSearch, BorderLayout.CENTER);
        searchBox.add(new JLabel(" 🔍 "), BorderLayout.EAST);
        searchBox.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        p.add(searchBox, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private void setupActions() {
        btnAdd.addActionListener(e -> {
            if(!txtName.getText().isEmpty()){
                model.addRow(new Object[]{model.getRowCount()+1, txtName.getText(), txtPos.getText(), txtPhone.getText(), cbStatus.getSelectedItem()});
            }
        });
        btnClear.addActionListener(e -> {
            txtName.setText(""); txtPos.setText(""); txtPhone.setText("");
        });
        btnDel.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r != -1) model.removeRow(r);
        });
    }

    private JButton createStyledBtn(String text, Color bg) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(110, 35));
        b.setBackground(bg);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        return b;
    }
}