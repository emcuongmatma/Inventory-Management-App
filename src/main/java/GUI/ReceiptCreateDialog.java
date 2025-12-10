package GUI;

import BUS.ProductBUS;
import BUS.ReceiptBUS;
import BUS.SupplierBUS;
import DTO.ProductDTO;
import DTO.ReceiptItemDTO;
import DTO.SupplierDTO;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReceiptCreateDialog extends JDialog {

    private JComboBox<String> cbProduct;
    private JComboBox<String> cbSupplier; 
    private JTextField txtQty, txtPrice, txtNote;
    private DefaultTableModel modelCart;
    private JTable tableCart;
    private JLabel lblTotal;
    private JButton btnAdd, btnSave;

    private List<ReceiptItemDTO> cart = new ArrayList<>();
    private List<ProductDTO> products;

    private SupplierBUS supplierBUS = new SupplierBUS();
 
    
    private final DecimalFormat df = new DecimalFormat("###,###");
    private boolean isSuccess = false;

    private static final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private static final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);
    private static final Color COLOR_BG = new Color(245, 247, 250);
    private static final Color COLOR_TEXT = new Color(100, 100, 100);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    public ReceiptCreateDialog(JFrame parent) {
        super(parent, "Tạo Phiếu Nhập Kho", true);
        initUI();
        initEvents();
        loadData();
    }

    private void initUI() {
        setSize(1100, 650);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        add(createLeftPanel(), BorderLayout.WEST);
        add(createRightPanel(), BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setPreferredSize(new Dimension(380, 0));

        JLabel lblHeader = new JLabel("THÔNG TIN NHẬP", JLabel.LEFT);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setForeground(COLOR_PRIMARY);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        cbSupplier = new JComboBox<>();
 
        
        txtNote = new JTextField();
        cbProduct = new JComboBox<>();
        txtPrice = new JTextField();
        txtQty = new JTextField();

        styleControl(cbSupplier); // Style cho ComboBox Supplier
        styleControl(txtNote);
        styleControl(cbProduct);
        styleControl(txtPrice);
        styleControl(txtQty);

        btnAdd = createBtn("Thêm vào danh sách", COLOR_SUCCESS);
        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        pnlLeft.add(lblHeader);
        pnlLeft.add(Box.createVerticalStrut(20));

        pnlLeft.add(createInputGroup("Chọn Nhà Cung Cấp:", cbSupplier));

        
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(createInputGroup("Ghi Chú:", txtNote));
        pnlLeft.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        pnlLeft.add(sep);
        pnlLeft.add(Box.createVerticalStrut(20));

        pnlLeft.add(createInputGroup("Chọn Sản Phẩm:", cbProduct));
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(createInputGroup("Đơn Giá Nhập (VND):", txtPrice));
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(createInputGroup("Số Lượng:", txtQty));
        pnlLeft.add(Box.createVerticalStrut(25));
        pnlLeft.add(btnAdd);

        return pnlLeft;
    }

    private JPanel createRightPanel() {
        JPanel pnlRight = new JPanel(new BorderLayout(0, 15));
        pnlRight.setBorder(new EmptyBorder(20, 0, 20, 20));
        pnlRight.setBackground(COLOR_BG);

        modelCart = new DefaultTableModel(new String[]{"Mã SP", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCart = new JTable(modelCart);
        styleTable(tableCart);

        JScrollPane scrollPane = new JScrollPane(tableCart);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));

        JPanel pnlBotRight = new JPanel(new BorderLayout());
        pnlBotRight.setBackground(COLOR_BG);

        lblTotal = new JLabel("Tổng tiền: 0 VND", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(COLOR_DANGER);
        lblTotal.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnSave = createBtn("LƯU PHIẾU & NHẬP KHO", COLOR_PRIMARY);
        btnSave.setPreferredSize(new Dimension(200, 50));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));

        pnlBotRight.add(lblTotal, BorderLayout.NORTH);
        pnlBotRight.add(btnSave, BorderLayout.EAST);

        pnlRight.add(scrollPane, BorderLayout.CENTER);
        pnlRight.add(pnlBotRight, BorderLayout.SOUTH);

        return pnlRight;
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> handleAdd());
        btnSave.addActionListener(e -> handleSave());
    }

    private void handleAdd() {
        try {
            if (cbProduct.getSelectedIndex() < 0) return;

            String itemStr = cbProduct.getSelectedItem().toString();
            String[] parts = itemStr.split(" - ", 2);
            String code = parts[0];
            String name = parts.length > 1 ? parts[1] : code;

            String qtyStr = txtQty.getText().trim();
            String priceStr = txtPrice.getText().trim();

            if (qtyStr.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ số lượng và giá!");
                return;
            }

            int qty = Integer.parseInt(qtyStr);
            int price = Integer.parseInt(priceStr);

            if (qty <= 0 || price < 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải > 0 và giá >= 0");
                return;
            }

            boolean exists = false;
            for (ReceiptItemDTO item : cart) {
                if (item.getProductCode().equals(code)) {
                    item.setQuantity(item.getQuantity() + qty);
                    item.setUnitPrice(price);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                cart.add(new ReceiptItemDTO(code, name, qty, price));
            }

            updateTable();
            txtQty.setText("");
            txtPrice.setText("");
            txtQty.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void handleSave() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách nhập hàng đang trống!");
            return;
        }


        if (cbSupplier.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Nhà Cung Cấp!");
            return;
        }

        String supplierStr = cbSupplier.getSelectedItem().toString();
        String supplierCode = supplierStr.split(" - ")[0]; 


        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận nhập kho " + cart.size() + " mặt hàng từ " + supplierStr + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean result = ReceiptBUS.getInstance().addNewReceipt(
                    supplierCode,
                    txtNote.getText().trim(),
                    cart
            );

            if (result) {
                JOptionPane.showMessageDialog(this, "Nhập kho thành công!");
                isSuccess = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu! Vui lòng kiểm tra lại.");
            }
        }
    }

    private void loadData() {
        loadProducts();
        loadSuppliers();
    }

    private void loadProducts() {
        cbProduct.removeAllItems();
        products = ProductBUS.getInstance().getAllProducts();
        if (products != null) {
            for (ProductDTO p : products) {
                cbProduct.addItem(p.getProductCode() + " - " + p.getName());
            }
        }
    }
    
    private void loadSuppliers() {
        cbSupplier.removeAllItems();
        List<SupplierDTO> suppliers = supplierBUS.getAllSuppliers();
        if (suppliers != null) {
            for (SupplierDTO s : suppliers) {
                // Hiển thị dạng: SP001 - Công ty ABC
                cbSupplier.addItem(s.getSupplierCode() + " - " + s.getName());
            }
        }
    }

    private void updateTable() {
        modelCart.setRowCount(0);
        long total = 0;
        for (ReceiptItemDTO i : cart) {
            long sum = (long) i.getQuantity() * i.getUnitPrice();
            total += sum;
            modelCart.addRow(new Object[]{
                    i.getProductCode(),
                    i.getName(),
                    i.getQuantity(),
                    df.format(i.getUnitPrice()),
                    df.format(sum)
            });
        }
        lblTotal.setText("Tổng tiền: " + df.format(total) + " VND");
    }

    private JPanel createInputGroup(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT);

        p.add(lbl, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void styleControl(JComponent c) {
        c.setFont(FONT_PLAIN);
        c.setPreferredSize(new Dimension(100, 35));
        c.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 8, 5, 8)
        ));
        if (c instanceof JComboBox) c.setBackground(Color.WHITE);
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_PLAIN);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setFillsViewportHeight(true);
         table.setShowVerticalLines(true); 
        table.setShowHorizontalLines(true); 
        table.setGridColor(new Color(230, 230, 230));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(240, 242, 245));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(0, 35));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(2).setCellRenderer(center);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0, 0, 0, 10));
        table.getColumnModel().getColumn(3).setCellRenderer(right);
        table.getColumnModel().getColumn(4).setCellRenderer(right);
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}