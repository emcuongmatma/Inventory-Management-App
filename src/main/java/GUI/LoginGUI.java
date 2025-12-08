package GUI;

import BUS.AdminBUS;
import DTO.AdminDTO;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    private static final Color PRIMARY_COLOR = new Color(25, 42, 86);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(100, 100, 100);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Component.focusWidth", 1);
        } catch(Exception e) { 
            e.printStackTrace(); 
        }
        new LoginGUI().setVisible(true);
    }

    public LoginGUI() {
        initUI();
        initData();
        initEvents();
    }

    private void initData() {
        try {
            AdminBUS.getInstance(); 
        } catch (Exception e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
        }
    }

    private void initUI() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(createLeftPanel());
        mainPanel.add(createRightPanel());

        setContentPane(mainPanel);
    }

    private JPanel createLeftPanel() {
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

        pLeft.add(Box.createVerticalGlue());
        pLeft.add(lblIcon);
        pLeft.add(Box.createVerticalStrut(30));
        pLeft.add(Box.createVerticalGlue());

        return pLeft;
    }

    private JPanel createRightPanel() {
        JPanel pRight = new JPanel(new GridBagLayout());
        pRight.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblLogin = new JLabel("ĐĂNG NHẬP");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLogin.setForeground(PRIMARY_COLOR);
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        pRight.add(lblLogin, gbc);

        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = 1;

        JLabel lblUser = new JLabel("Tên đăng nhập");
        lblUser.setFont(FONT_BOLD);
        lblUser.setForeground(TEXT_COLOR);
        gbc.gridy = 1; 
        pRight.add(lblUser, gbc);

        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(320, 45));
        txtUser.putClientProperty("JTextField.placeholderText", "Nhập tài khoản");
        txtUser.putClientProperty("JTextField.showClearButton", true);
        txtUser.setFont(FONT_PLAIN);
        gbc.gridy = 2; 
        pRight.add(txtUser, gbc);

        JLabel lblPass = new JLabel("Mật khẩu");
        lblPass.setFont(FONT_BOLD);
        lblPass.setForeground(TEXT_COLOR);
        gbc.gridy = 3; 
        gbc.insets = new Insets(15, 0, 5, 0);
        pRight.add(lblPass, gbc);

        txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(320, 45));
        txtPass.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu");
        txtPass.putClientProperty("JPasswordField.showRevealButton", true);
        txtPass.setFont(FONT_PLAIN);
        gbc.gridy = 4; 
        gbc.insets = new Insets(5, 0, 5, 0);
        pRight.add(txtPass, gbc);

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setPreferredSize(new Dimension(320, 50));
        btnLogin.setBackground(PRIMARY_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 5;
        gbc.insets = new Insets(40, 0, 0, 0);
        pRight.add(btnLogin, gbc);

        JLabel lblCopy = new JLabel("© 2025 Computer Store System");
        lblCopy.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopy.setForeground(new Color(200, 200, 200));
        lblCopy.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(60, 0, 0, 0);
        pRight.add(lblCopy, gbc);

        return pRight;
    }

    private void initEvents() {
        btnLogin.addActionListener(e -> checkLogin());

        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) checkLogin();
            }
        };
        txtUser.addKeyListener(enterKey);
        txtPass.addKeyListener(enterKey);

        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogin.setBackground(SECONDARY_COLOR); }
            public void mouseExited(MouseEvent e) { btnLogin.setBackground(PRIMARY_COLOR); }
        });
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