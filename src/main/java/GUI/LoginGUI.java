package GUI;

import BUS.AdminBUS;
import DTO.AdminDTO;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    // Màu sắc chủ đạo
    private final Color PRIMARY_COLOR = new Color(25, 42, 86); // Xanh đậm
    private final Color SECONDARY_COLOR = new Color(52, 152, 219); // Xanh sáng
    private final Color TEXT_COLOR = new Color(100, 100, 100); // Xám đậm cho label

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            // Cấu hình bo góc cho toàn bộ component
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.focusWidth", 1);
        } catch(Exception e) { e.printStackTrace(); }
        
        new LoginGUI().setVisible(true);
    }

    public LoginGUI() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG");
        setSize(900, 550); // Kích thước rộng hơn để chia đôi đẹp
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Gọi BUS để đảm bảo admin mặc định được tạo (nếu chưa có) - LOGIC CŨ GIỮ NGUYÊN
        try {
            AdminBUS.getInstance(); 
        } catch (Exception e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
        }

        // Layout chính chia đôi màn hình
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        setContentPane(mainPanel);

       
        JPanel pLeft = new JPanel();
        pLeft.setBackground(PRIMARY_COLOR);
        pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
        
      
        JLabel lblIcon = new JLabel("<html><div style='text-align: center; color: white;'>" +
                "<span style='font-size: 80px;'>💻</span><br>" + 
                "<br>" +
                "<span style='font-size: 32px; font-weight: bold;'>COMPUTER</span><br>" +
                "<span style='font-size: 26px;'>STORE MANAGER</span>" +
                "</div></html>", SwingConstants.CENTER);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        java.net.URL iconURL = getClass().getResource("./icon/laptop-icon.png");
        
//        JLabel lblDesc = new JLabel("<html><div style='text-align: center; color: #bdc3c7; margin-top: 20px;'>" +
//                "Hệ thống quản lý kho chuyên nghiệp<br>" +
//                "Hiệu quả - Chính xác - Bảo mật" +
//                "</div></html>");
//        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
//        lblDesc.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        // Căn giữa theo chiều dọc
        pLeft.add(Box.createVerticalGlue());
        pLeft.add(lblIcon);
        pLeft.add(Box.createVerticalStrut(30));
//        pLeft.add(lblDesc);
        pLeft.add(Box.createVerticalGlue());

  
        JPanel pRight = new JPanel(new GridBagLayout());
        pRight.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tiêu đề form
        JLabel lblLogin = new JLabel("ĐĂNG NHẬP");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLogin.setForeground(PRIMARY_COLOR);
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0); // Cách xa form bên dưới
        pRight.add(lblLogin, gbc);

        // Reset insets cho các ô nhập liệu
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = 1;

        // --- Ô NHẬP TÀI KHOẢN ---
        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(TEXT_COLOR);
        gbc.gridy = 1; pRight.add(lblUser, gbc);

        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(320, 45)); // Ô to hơn
        txtUser.putClientProperty("JTextField.placeholderText", "Nhập tài khoản");
        txtUser.putClientProperty("JTextField.showClearButton", true);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2; pRight.add(txtUser, gbc);

        // --- Ô NHẬP MẬT KHẨU ---
        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(TEXT_COLOR);
        gbc.gridy = 3; 
        gbc.insets = new Insets(15, 0, 5, 0); // Khoảng cách với ô trên
        pRight.add(lblPass, gbc);

        txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(320, 45));
        txtPass.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
        txtPass.putClientProperty("JPasswordField.showRevealButton", true); 
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4; 
        gbc.insets = new Insets(5, 0, 5, 0);
        pRight.add(txtPass, gbc);

        // --- NÚT ĐĂNG NHẬP ---
        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setPreferredSize(new Dimension(320, 50));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover đổi màu nút
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(SECONDARY_COLOR); }
            public void mouseExited(MouseEvent e) { btnLogin.setBackground(PRIMARY_COLOR); }
        });

        gbc.gridy = 5;
        gbc.insets = new Insets(40, 0, 0, 0); // Cách xa form trên
        pRight.add(btnLogin, gbc);
        
        // Footer (Copyright)
        JLabel lblCopy = new JLabel("© 2025 Computer Store System");
        lblCopy.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopy.setForeground(new Color(200, 200, 200));
        lblCopy.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(60, 0, 0, 0);
        pRight.add(lblCopy, gbc);

        // Thêm 2 panel vào main panel
        mainPanel.add(pLeft);
        mainPanel.add(pRight);

        // ====================================================================
        // LOGIC XỬ LÝ SỰ KIỆN (GIỮ NGUYÊN NHƯ CŨ)
        // ====================================================================
        btnLogin.addActionListener(e -> checkLogin());
        
        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) checkLogin();
            }
        };
        txtUser.addKeyListener(enterKey);
        txtPass.addKeyListener(enterKey);
    }

    private void checkLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AdminDTO admin = AdminBUS.getInstance().login(user, pass);
            if (admin != null) {
                this.dispose();
               
                EventQueue.invokeLater(() -> new MainGUI().setVisible(true)); 
                
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}