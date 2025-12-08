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

    private ProductGUI productGUI;
    private BrandGUI brandGUI;
    private CategoryGUI categoryGUI;
    private ReceiptGUI receiptGUI;
    private ExportGUI exportGUI;
    private StartsGUI statsGUI;
    private CustomerGUI customerGUI;

    private List<MenuButton> menuButtons = new ArrayList<>();
    private MenuButton activeBtn = null;

    private static final Color COL_MENU_BG = new Color(25, 42, 86);
    private static final Color COL_MENU_HOVER = new Color(44, 62, 80);
    private static final Color COL_MENU_ACTIVE = new Color(52, 152, 219);
    private static final Color COL_TEXT_WHITE = Color.WHITE;
    private static final Font FONT_MENU = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_LOGO = new Font("Segoe UI", Font.BOLD, 22);

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("TableHeader.height", 40);
            UIManager.put("Table.rowHeight", 35);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        EventQueue.invokeLater(() -> new MainGUI().setVisible(true));
    }

    public MainGUI() {
        initUI();
    }

    private void initUI() {
        setTitle("HỆ THỐNG QUẢN LÝ KHO MÁY TÍNH");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        initSidebar();
        initContent();

        if (!menuButtons.isEmpty()) {
            setActiveButton(menuButtons.get(0));
        }
    }

    private void initSidebar() {
        pnlMenu = new JPanel(new BorderLayout());
        pnlMenu.setBackground(COL_MENU_BG);
        pnlMenu.setPreferredSize(new Dimension(260, 0));

        pnlMenu.add(createLogoPanel(), BorderLayout.NORTH);
        pnlMenu.add(createMenuList(), BorderLayout.CENTER);
        pnlMenu.add(createBottomPanel(), BorderLayout.SOUTH);

        contentPane.add(pnlMenu, BorderLayout.WEST);
    }

    private JPanel createLogoPanel() {
        JPanel pnlLogo = new JPanel(new BorderLayout());
        pnlLogo.setBackground(COL_MENU_BG);
        pnlLogo.setPreferredSize(new Dimension(260, 140));
        pnlLogo.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblIcon = new JLabel("💻", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        lblIcon.setForeground(COL_TEXT_WHITE);

        JLabel lblTitle = new JLabel("<html><center>KHO MÁY TÍNH<br><span style='font-size:11px; color:#bdc3c7'>MANAGER SYSTEM</span></center></html>", SwingConstants.CENTER);
        lblTitle.setForeground(COL_TEXT_WHITE);
        lblTitle.setFont(FONT_LOGO);

        pnlLogo.add(lblIcon, BorderLayout.CENTER);
        pnlLogo.add(lblTitle, BorderLayout.SOUTH);
        return pnlLogo;
    }

    private JPanel createMenuList() {
        JPanel pnlList = new JPanel();
        pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
        pnlList.setBackground(COL_MENU_BG);
        pnlList.setBorder(new EmptyBorder(10, 10, 10, 10));

        addMenuButton(pnlList, "  SẢN PHẨM", "PRODUCT");
        addMenuButton(pnlList, "  THƯƠNG HIỆU", "BRAND");
        addMenuButton(pnlList, "  DANH MỤC", "CATEGORY");
        addMenuButton(pnlList, "  NHẬP KHO", "RECEIPT");
        addMenuButton(pnlList, "  XUẤT KHO", "EXPORT");
        addMenuButton(pnlList, "  THỐNG KÊ", "STATS");
        addMenuButton(pnlList, "  KHÁCH HÀNG", "CUSTOMER");

        return pnlList;
    }

    private JPanel createBottomPanel() {
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.setBackground(COL_MENU_BG);
        pnlBottom.setBorder(new EmptyBorder(0, 0, 20, 0));

        JButton btnExit = new JButton("THOÁT HỆ THỐNG");
        btnExit.setPreferredSize(new Dimension(220, 45));
        btnExit.setBackground(new Color(231, 76, 60));
        btnExit.setForeground(COL_TEXT_WHITE);
        btnExit.setFont(FONT_MENU);
        btnExit.setFocusPainted(false);
        btnExit.setBorderPainted(false);
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(e -> System.exit(0));

        pnlBottom.add(btnExit);
        return pnlBottom;
    }

    private void initContent() {
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        pnlContent.setBackground(new Color(245, 247, 250));

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

        contentPane.add(pnlContent, BorderLayout.CENTER);
    }

    private void addMenuButton(JPanel panel, String text, String key) {
        MenuButton btn = new MenuButton(text, key);
        menuButtons.add(btn);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private void setActiveButton(MenuButton btn) {
        if (activeBtn != null) {
            activeBtn.setActive(false);
        }
        activeBtn = btn;
        activeBtn.setActive(true);

        cardLayout.show(pnlContent, btn.getKey());
        refreshData(btn.getKey());
    }

    private void refreshData(String key) {
        switch (key) {
            case "PRODUCT": productGUI.loadData(); break;
            case "BRAND": brandGUI.loadData(); break;
            case "CATEGORY": categoryGUI.loadData(); break;
            case "RECEIPT": receiptGUI.loadData(); break;
            case "EXPORT": exportGUI.loadData(); break;
            case "STATS": statsGUI.loadData(); break;
        }
    }

    private class MenuButton extends JButton {
        private String key;
        private boolean isActive = false;

        public MenuButton(String text, String key) {
            super(text);
            this.key = key;
            
            setFont(FONT_MENU);
            setForeground(new Color(200, 200, 200));
            setBackground(COL_MENU_BG);
            setBorder(new EmptyBorder(10, 20, 10, 10));
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isActive) {
                        setBackground(COL_MENU_HOVER);
                        setForeground(COL_TEXT_WHITE);
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isActive) {
                        setBackground(COL_MENU_BG);
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
                setBackground(COL_MENU_ACTIVE);
                setForeground(COL_TEXT_WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 15));
            } else {
                setBackground(COL_MENU_BG);
                setForeground(new Color(200, 200, 200));
                setFont(FONT_MENU);
            }
            repaint();
        }

        public String getKey() {
            return key;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isActive || getBackground().equals(COL_MENU_HOVER)) {
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                if (isActive) {
                    g2.setColor(COL_TEXT_WHITE);
                    g2.fillRoundRect(0, 10, 5, getHeight() - 20, 5, 5);
                }
            }
            super.paintComponent(g);
        }
    }
}