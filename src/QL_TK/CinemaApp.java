package QL_TK;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CinemaApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel activeMenu;

    public CinemaApp() {
        setTitle("Quản Lý Tài Khoản Nhân Viên");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(255, 193, 7));
        sidebar.setPreferredSize(new Dimension(85, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        EmployeeView empPage = new EmployeeView(); 
        cardPanel.add(empPage, "EMP_PAGE"); 
        
        cardPanel.add(new JLabel("Màn hình Trang chủ", SwingConstants.CENTER), "HOME_PAGE");
        cardPanel.add(new JLabel("Màn hình Phim", SwingConstants.CENTER), "FILM_PAGE");

        sidebar.add(createMenu("🏠", "HOME_PAGE"));
        sidebar.add(createMenu("🎬", "FILM_PAGE"));
        
        JPanel empMenu = createMenu("👥", "EMP_PAGE");
        sidebar.add(empMenu);
       
        activeMenu = empMenu;
        empMenu.setOpaque(true);
        empMenu.setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "EMP_PAGE");
    }

    private JPanel createMenu(String icon, String cardName) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setPreferredSize(new Dimension(85, 85));
        p.setOpaque(false);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lbl = new JLabel(icon);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 35));
        p.add(lbl);

        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               
                if(activeMenu != null) {
                    activeMenu.setOpaque(false);
                    activeMenu.setBackground(null);
                }
                activeMenu = p;
                p.setOpaque(true);
                p.setBackground(Color.WHITE);
                
                cardLayout.show(cardPanel, cardName);
                
              
                cardPanel.revalidate();
                cardPanel.repaint();
            }
        });
        return p;
    }

    public static void main(String[] args) {
    
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e){}
        
        SwingUtilities.invokeLater(() -> {
            CinemaApp app = new CinemaApp();
            app.setVisible(true);
        });
    }
}