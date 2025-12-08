package GUI;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {

    private JPanel contentPane;
    private JPanel pnlMenu;
    private JPanel pnlContent;
    private CardLayout cardLayout;

    // Các màn hình chức năng
    private ProductGUI productGUI;
    private BrandGUI brandGUI;
    private CategoryGUI categoryGUI;
    private ReceiptGUI receiptGUI;
    private ExportGUI exportGUI;
    private StartsGUI statsGUI;
    private CustomerGUI customerGUI;

    // Quản lý trạng thái nút menu
    private List<MenuButton> menuButtons = new ArrayList<>();
    private MenuButton activeBtn = null;

    // Màu sắc chủ đạo
    private final Color COLOR_MENU_BG = new Color(25, 42, 86); // Xanh đậm
    private final Color COLOR_MENU_HOVER = new Color(44, 62, 80); // Xanh sáng hơn chút
    private final Color COLOR_MENU_ACTIVE = new Color(52, 152, 219); // Xanh dương nổi bật
    private final Color COLOR_TEXT = Color.WHITE;

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            // Config giao diện đẹp global
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("TableHeader.height", 40);
            UIManager.put("Table.rowHeight", 35); // Tăng chiều cao dòng bảng mặc định
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EventQueue.invokeLater(() -> new MainGUI().setVisible(true));
    }

    public MainGUI() {
        setTitle("HỆ THỐNG QUẢN LÝ KHO MÁY TÍNH");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850); // Tăng kích thước chút cho thoáng
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        initSidebar();
        initContent();
    }

    private void initSidebar() {
        pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(COLOR_MENU_BG);
        pnlMenu.setPreferredSize(new Dimension(260, 0));

        // --- 1. LOGO AREA ---
        JPanel pnlLogo = new JPanel(new BorderLayout());
        pnlLogo.setBackground(COLOR_MENU_BG);
        pnlLogo.setPreferredSize(new Dimension(260, 140));
        pnlLogo.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblIcon = new JLabel("💻", SwingConstants.CENTER); // Icon minh họa
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblIcon.setForeground(Color.WHITE);
        
        JLabel lblTitle = new JLabel("<html><center>KHO MÁY TÍNH<br><span style='font-size:11px; color:#bdc3c7'>MANAGER SYSTEM</span></center></html>", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        pnlLogo.add(lblIcon, BorderLayout.CENTER);
        pnlLogo.add(lblTitle, BorderLayout.SOUTH);
        
        pnlMenu.add(pnlLogo, BorderLayout.NORTH);

        // --- 2. MENU LIST ---
        JPanel pnlList = new JPanel();
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
        pnlList.setBackground(COLOR_MENU_BG);
        pnlList.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding xung quanh nút

        // Thêm các nút menu (Dùng Unicode Icon để không cần file ảnh)
        addMenuButton(pnlList, "  SẢN PHẨM", "PRODUCT");
        addMenuButton(pnlList, "️  THƯƠNG HIỆU", "BRAND");
        addMenuButton(pnlList, "  DANH MỤC", "CATEGORY");
       // pnlList.add(Box.createRigidArea(new Dimension(0, 15))); // Khoảng cách nhóm
        addMenuButton(pnlList, "  NHẬP KHO", "RECEIPT");
        addMenuButton(pnlList, "  XUẤT KHO", "EXPORT");
       // pnlList.add(Box.createRigidArea(new Dimension(0, 15)));
       // addMenuButton(pnlList, "  KHÁCH HÀNG", "CUSTOMER");
        addMenuButton(pnlList, "  THỐNG KÊ", "STATS");

        pnlMenu.add(pnlList, BorderLayout.CENTER);

        // --- 3. BOTTOM AREA (EXIT) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(COLOR_MENU_BG);
        pnlBottom.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton btnExit = new JButton("THOÁT HỆ THỐNG");
        btnExit.setPreferredSize(new Dimension(220, 45));
        btnExit.setBackground(new Color(231, 76, 60)); // Màu đỏ
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(e -> System.exit(0));

        pnlBottom.add(btnExit);
        pnlMenu.add(pnlBottom, BorderLayout.SOUTH);

        contentPane.add(pnlMenu, BorderLayout.WEST);
    }

    private void initContent() {
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(new Color(245, 247, 250)); // Màu nền nội dung sáng nhẹ

        // Khởi tạo các panel (Lazy loading hoặc tạo hết như bạn đang làm)
        productGUI = new ProductGUI();
        brandGUI = new BrandGUI();
        categoryGUI = new CategoryGUI();
        receiptGUI = new ReceiptGUI();
        exportGUI = new ExportGUI();
        statsGUI = new StartsGUI();
        customerGUI = new CustomerGUI();

        pnlContent.add(productGUI, "PRODUCT");
        pnlContent.add(brandGUI, "BRAND");
        pnlContent.add(categoryGUI, "CATEGORY");
        pnlContent.add(receiptGUI, "RECEIPT");
        pnlContent.add(exportGUI, "EXPORT");
        pnlContent.add(statsGUI, "STATS");
        pnlContent.add(customerGUI, "CUSTOMER");

        // Set mặc định chọn tab đầu tiên
        if (!menuButtons.isEmpty()) {
            setActiveButton(menuButtons.get(0));
        }

        contentPane.add(pnlContent, BorderLayout.CENTER);
    }

    // Hàm tạo nút menu tùy chỉnh
    private void addMenuButton(JPanel panel, String text, String key) {
        MenuButton btn = new MenuButton(text, key);
        menuButtons.add(btn);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 8))); // Khoảng cách giữa các nút
    }

    // Hàm xử lý logic chọn nút
    private void setActiveButton(MenuButton btn) {
        if (activeBtn != null) {
            activeBtn.setActive(false);
        }
        activeBtn = btn;
        activeBtn.setActive(true);
        
        // Chuyển card
        cardLayout.show(pnlContent, btn.getKey());
        
        // Refresh data
        String key = btn.getKey();
        if(key.equals("PRODUCT")) productGUI.loadData();
        else if(key.equals("BRAND")) brandGUI.loadData();
        else if(key.equals("CATEGORY")) categoryGUI.loadData();
        else if(key.equals("RECEIPT")) receiptGUI.loadData();
        else if(key.equals("EXPORT")) exportGUI.loadData();
        else if(key.equals("STATS")) statsGUI.loadData();
        
    }

    // ========================================================================
    // CLASS CUSTOM BUTTON ĐỂ TẠO GIAO DIỆN ĐẸP
    // ========================================================================
    private class MenuButton extends JButton {
        private String key;
        private boolean isActive = false;

        public MenuButton(String text, String key) {
            super(text);
            this.key = key;
            
            // Style cơ bản
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(new Color(200, 200, 200)); // Màu chữ mặc định hơi xám
            setBackground(COLOR_MENU_BG);
            setBorder(new EmptyBorder(10, 20, 10, 10));
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setContentAreaFilled(false); // Tự vẽ background
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            // Hover Effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isActive) {
                        setBackground(COLOR_MENU_HOVER);
                        setForeground(Color.WHITE);
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isActive) {
                        setBackground(COLOR_MENU_BG);
                        setForeground(new Color(200, 200, 200));
                        repaint();
                    }
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    setActiveButton(MenuButton.this);
                }
            });
        }

        public void setActive(boolean active) {
            this.isActive = active;
            if (active) {
                setBackground(COLOR_MENU_ACTIVE); // Màu xanh sáng khi chọn
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15)); // Chữ to hơn chút
            } else {
                setBackground(COLOR_MENU_BG);
                setForeground(new Color(200, 200, 200));
                setFont(new Font("Segoe UI", Font.BOLD, 14));
            }
            repaint();
        }

        public String getKey() { return key; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isActive || getBackground().equals(COLOR_MENU_HOVER)) {
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Bo góc nút
                
                // Vẽ vạch chỉ thị bên trái khi Active
                if (isActive) {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 10, 5, getHeight() - 20, 5, 5);
                }
            }

            super.paintComponent(g);
        }
    }
}