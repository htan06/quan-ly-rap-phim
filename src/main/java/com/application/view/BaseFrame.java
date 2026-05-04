package com.application.view;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    public BaseFrame(String title) {
        setTitle(title);

        // Kích thước mặc định rộng rãi (có thể ghi đè ở các lớp con như LoginFrame)
        setSize(1000, 700);

        // Canh giữa màn hình
        setLocationRelativeTo(null);

        // Đóng chương trình khi tắt cửa sổ
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Sử dụng font hệ thống hiện đại cho toàn bộ frame
        setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Thiết lập màu nền từ ThemeConfig hoặc mặc định là màu trắng/xám nhạt web
        // Nếu ThemeConfig.BACKGROUND của bạn là màu tối, hãy đổi thành Color.WHITE để hợp style hiện đại
        if (ThemeConfig.BACKGROUND != null) {
            getContentPane().setBackground(ThemeConfig.BACKGROUND);
        } else {
            getContentPane().setBackground(Color.WHITE);
        }

        // Khử răng cưa cho chữ (Anti-aliasing) giúp giao diện mượt mà hơn
        // Lưu ý: Trong Swing, việc này thường được xử lý ở cấp độ Component,
        // nhưng thiết lập mặc định ở Frame là một khởi đầu tốt.
    }

    /**
     * Phương thức hỗ trợ để thay đổi nhanh Icon của ứng dụng nếu cần
     */
    public void setAppIcon(String path) {
        try {
            ImageIcon img = new ImageIcon(path);
            setIconImage(img.getImage());
        } catch (Exception e) {
            System.out.println("Không tìm thấy icon ứng dụng.");
        }
    }
}