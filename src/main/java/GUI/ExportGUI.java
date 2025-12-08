package GUI;

import BUS.ExportBUS;
import BUS.ProductBUS;
import DTO.ExportDTO;
import DTO.ProductDTO;
import DTO.ReceiptItemDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExportGUI extends JPanel {

    private JComboBox<String> cbProduct;
    private JTextField txtQty, txtCustomer;
    private DefaultTableModel modelCart, modelHistory;
    private JTable tableCart, tableHistory;
    private JButton btnAdd, btnPay, btnRefresh;
    
    private List<ReceiptItemDTO> cart = new ArrayList<>();
    private List<ProductDTO> products;
    private final DecimalFormat df = new DecimalFormat("###,###");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final Color COLOR_BG = new Color(240, 242, 245);
    private static final Color COLOR_BLUE = new Color(66, 133, 244);
    private static final Color COLOR_GREEN = new Color(22, 160, 133);
    private static final Color COLOR_TEXT = new Color(50, 50, 50);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    public ExportGUI() {
        initUI();
        initEvents();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pMain = new JPanel(new GridLayout(1, 2, 20, 0));
        pMain.setOpaque(false);
        
        pMain.add(createLeftPanel());
        pMain.add(createRightPanel());

        add(pMain, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel pLeft = new JPanel(new BorderLayout(10, 10));
        pLeft.setBackground(Color.WHITE);
        pLeft.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("TẠO PHIẾU XUẤT KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));

        JPanel pForm = new JPanel();
        pForm.setLayout(new BoxLayout(pForm, BoxLayout.Y_AXIS));
        pForm.setBackground(Color.WHITE);

        cbProduct = new JComboBox<>();
        txtQty = new JTextField();
        txtCustomer = new JTextField();
        styleControl(cbProduct);
        styleControl(txtQty);
        styleControl(txtCustomer);

        pForm.add(createInputGroup("Sản Phẩm:", cbProduct));
        pForm.add(Box.createVerticalStrut(10));
        pForm.add(createInputGroup("Số Lượng Bán:", txtQty));
        pForm.add(Box.createVerticalStrut(10));
        pForm.add(createInputGroup("Tên Khách Hàng:", txtCustomer));
        pForm.add(Box.createVerticalStrut(15));

        btnAdd = new JButton("Thêm vào giỏ");
        styleButton(btnAdd, COLOR_BLUE);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        pForm.add(btnAdd);

        modelCart = new DefaultTableModel(new String[]{"Sản phẩm", "SL", "Đơn giá", "Thành tiền"}, 0);
        tableCart = new JTable(modelCart);
        styleTable(tableCart);
        
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.getViewport().setBackground(Color.WHITE);
        scrollCart.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel pCenter = new JPanel(new BorderLayout(0, 15));
        pCenter.setBackground(Color.WHITE);
        pCenter.add(pForm, BorderLayout.NORTH);
        pCenter.add(scrollCart, BorderLayout.CENTER);

        btnPay = new JButton("THANH TOÁN & XUẤT KHO");
        styleButton(btnPay, COLOR_GREEN);
        btnPay.setPreferredSize(new Dimension(100, 50));

        pLeft.add(lblTitle, BorderLayout.NORTH);
        pLeft.add(pCenter, BorderLayout.CENTER);
        pLeft.add(btnPay, BorderLayout.SOUTH);

        return pLeft;
    }

    private JPanel createRightPanel() {
        JPanel pRight = new JPanel(new BorderLayout(10, 10));
        pRight.setBackground(Color.WHITE);
        pRight.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("LỊCH SỬ XUẤT KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));

        btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        btnRefresh.setPreferredSize(new Dimension(90, 30));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pHeader.add(lblTitle, BorderLayout.WEST);
        pHeader.add(btnRefresh, BorderLayout.EAST);

        modelHistory = new DefaultTableModel(new String[]{"Mã HĐ", "Khách Hàng", "Ngày Xuất", "Tổng Tiền"}, 0);
        tableHistory = new JTable(modelHistory);
        styleTable(tableHistory);
        
        JScrollPane scrollHistory = new JScrollPane(tableHistory);
        scrollHistory.getViewport().setBackground(Color.WHITE);
        scrollHistory.setBorder(BorderFactory.createEmptyBorder());

        pRight.add(pHeader, BorderLayout.NORTH);
        pRight.add(scrollHistory, BorderLayout.CENTER);

        return pRight;
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> handleAddCart());
        btnPay.addActionListener(e -> handlePayment());
        btnRefresh.addActionListener(e -> loadData());
    }

    private void handleAddCart() {
        try {
            int idx = cbProduct.getSelectedIndex();
            if (idx < 0 || products == null || idx >= products.size()) return;

            ProductDTO p = products.get(idx);
            String qtyText = txtQty.getText();
            
            if (qtyText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
                return;
            }

            int qty = Integer.parseInt(qtyText);
            if (qty > p.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Tồn kho không đủ! (Còn " + p.getQuantity() + ")");
                return;
            }

            cart.add(new ReceiptItemDTO(p.getProductCode(), p.getName(), qty, p.getPrice()));
            updateCart();
            txtQty.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handlePayment() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }
        String customer = txtCustomer.getText().trim();
        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
            return;
        }

        boolean ok = ExportBUS.getInstance().addNewExport(customer, "Xuất bán", cart);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Xuất kho thành công!");
            cart.clear();
            txtQty.setText("");
            txtCustomer.setText("");
            updateCart();
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi! Có thể hàng vừa hết hoặc lỗi hệ thống.");
        }
    }

    public void loadData() {
        cbProduct.removeAllItems();
        products = ProductBUS.getInstance().getAllProducts();
        if (products != null) {
            for (ProductDTO p : products) {
                cbProduct.addItem(p.getName() + " (Tồn: " + p.getQuantity() + ")");
            }
        }

        modelHistory.setRowCount(0);
        List<ExportDTO> history = ExportBUS.getInstance().getAllExports();
        if (history != null) {
            for (ExportDTO e : history) {
                modelHistory.addRow(new Object[]{
                    e.get_id(),
                    e.getCustomerCode(),
                    sdf.format(e.getExportDate()),
                    df.format(e.getTotalPrice())
                });
            }
        }
    }

    private void updateCart() {
        modelCart.setRowCount(0);
        long grandTotal = 0;
        for (ReceiptItemDTO i : cart) {
            long total = (long) i.getQuantity() * i.getUnitPrice();
            grandTotal += total;
            modelCart.addRow(new Object[]{
                i.getName(),
                i.getQuantity(),
                df.format(i.getUnitPrice()),
                df.format(total)
            });
        }
    }

    private JPanel createInputGroup(String title, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));

        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private void styleControl(JComponent c) {
        c.setFont(FONT_PLAIN);
        if (c instanceof JTextField || c instanceof JComboBox) {
            c.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 8, 5, 8)
            ));
        }
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(FONT_BOLD);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_PLAIN);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(COLOR_TEXT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
    }
}