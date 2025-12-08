package GUI;

import BUS.ProductBUS;
import BUS.ReceiptBUS;
import DTO.ProductDTO;
import DTO.ReceiptItemDTO;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReceiptCreateDialog extends JDialog {
    private JComboBox<String> cbProduct;
    private JTextField txtQty, txtPrice, txtSupplier, txtNote;
    private DefaultTableModel modelCart;
    private JTable tableCart;
    private List<ReceiptItemDTO> cart = new ArrayList<>();
    private List<ProductDTO> products;
    private JLabel lblTotal;
    private DecimalFormat df = new DecimalFormat("###,###");
    private boolean isSuccess = false;

    // Màu sắc chủ đạo
    private final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private final Color COLOR_DANGER = new Color(231, 76, 60);
    private final Color COLOR_BG = new Color(245, 247, 250);

    public ReceiptCreateDialog(JFrame parent) {
        super(parent, "Tạo Phiếu Nhập Kho", true);
        setSize(1100, 650); // Tăng kích thước để thoáng hơn
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        // --- LEFT PANEL: FORM NHẬP LIỆU ---
        JPanel pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setPreferredSize(new Dimension(380, 0));

        // Header Left
        JLabel lblHeaderLeft = new JLabel("THÔNG TIN NHẬP", JLabel.LEFT);
        lblHeaderLeft.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeaderLeft.setForeground(COLOR_PRIMARY);
        lblHeaderLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Inputs
        txtSupplier = new JTextField();
        txtNote = new JTextField();
        cbProduct = new JComboBox<>();
        txtPrice = new JTextField();
        txtQty = new JTextField();
        
        styleControl(txtSupplier);
        styleControl(txtNote);
        styleControl(cbProduct);
        styleControl(txtPrice);
        styleControl(txtQty);

        JButton btnAdd = createBtn("Thêm vào danh sách", COLOR_SUCCESS);
        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Add components to Left Panel
        pnlLeft.add(lblHeaderLeft);
        pnlLeft.add(Box.createVerticalStrut(20));
        pnlLeft.add(createInputGroup("Nhà Cung Cấp:", txtSupplier));
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

        // --- RIGHT PANEL: TABLE & TOTAL ---
        JPanel pnlRight = new JPanel(new BorderLayout(0, 15));
        pnlRight.setBorder(new EmptyBorder(20, 0, 20, 20));
        pnlRight.setBackground(COLOR_BG);

        // Table
        modelCart = new DefaultTableModel(new String[]{"Mã SP", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền"}, 0);
        tableCart = new JTable(modelCart);
        styleTable(tableCart);
        
        JScrollPane scrollPane = new JScrollPane(tableCart);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));

        // Bottom Right (Total + Save Button)
        JPanel pnlBotRight = new JPanel(new BorderLayout());
        pnlBotRight.setBackground(COLOR_BG);
        
        lblTotal = new JLabel("Tổng tiền: 0 VND", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(COLOR_DANGER);
        lblTotal.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton btnSave = createBtn("LƯU PHIẾU & NHẬP KHO", COLOR_PRIMARY);
        btnSave.setPreferredSize(new Dimension(200, 50));
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));

        pnlBotRight.add(lblTotal, BorderLayout.NORTH);
        pnlBotRight.add(btnSave, BorderLayout.EAST);

        pnlRight.add(scrollPane, BorderLayout.CENTER);
        pnlRight.add(pnlBotRight, BorderLayout.SOUTH);

        // --- ADD TO DIALOG ---
        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        // ====================================================================
        // === LOGIC XỬ LÝ ===
        // ====================================================================
        
        loadProducts();

        // 1. Logic Thêm vào giỏ
        btnAdd.addActionListener(e -> {
            try {
                if(cbProduct.getSelectedIndex() < 0) return;
                
                String itemStr = cbProduct.getSelectedItem().toString();
                // Giả định format: "CODE - Name" (VD: SP001 - iPhone 15)
                String[] parts = itemStr.split(" - ", 2);
                String code = parts[0];
                String name = parts.length > 1 ? parts[1] : code;
                
                String qtyStr = txtQty.getText().trim();
                String priceStr = txtPrice.getText().trim();

                if(qtyStr.isEmpty() || priceStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ số lượng và giá!");
                    return;
                }

                int qty = Integer.parseInt(qtyStr);
                int price = Integer.parseInt(priceStr);

                if(qty <= 0 || price < 0) {
                    JOptionPane.showMessageDialog(this, "Số lượng phải > 0 và giá >= 0");
                    return;
                }
                
                // Kiểm tra xem sản phẩm đã có trong giỏ chưa -> Cộng dồn
                boolean exists = false;
                for(ReceiptItemDTO item : cart) {
                    if(item.getProductCode().equals(code)) {
                        item.setQuantity(item.getQuantity() + qty);
                        item.setUnitPrice(price); // Cập nhật giá mới nhất
                        exists = true;
                        break;
                    }
                }
                
                if(!exists) {
                    cart.add(new ReceiptItemDTO(code, name, qty, price));
                }

                updateTable();
                
                // Reset input để nhập tiếp cho nhanh
                txtQty.setText("");
                txtPrice.setText("");
                txtQty.requestFocus(); // Focus lại ô số lượng
                
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
            } catch(Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        // 2. Logic Lưu phiếu
        btnSave.addActionListener(e -> {
            if(cart.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Danh sách nhập hàng đang trống!"); 
                return; 
            }
            if(txtSupplier.getText().trim().isEmpty()) { 
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên Nhà Cung Cấp!"); 
                txtSupplier.requestFocus();
                return; 
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Xác nhận nhập kho " + cart.size() + " mặt hàng?", 
                "Xác nhận", JOptionPane.YES_NO_OPTION);
                
            if(confirm == JOptionPane.YES_OPTION) {
                // Gọi BUS: BUS sẽ lo việc tạo phiếu VÀ tăng tồn kho (đã sửa ở bước trước)
                boolean result = ReceiptBUS.getInstance().addNewReceipt(
                    txtSupplier.getText().trim(), 
                    txtNote.getText().trim(), 
                    cart
                );
                
                if(result) {
                    JOptionPane.showMessageDialog(this, "Nhập kho thành công!");
                    isSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu! Vui lòng kiểm tra lại.");
                }
            }
        });
    }

    // ====================================================================
    // === HELPER METHODS ===
    // ====================================================================

    private void loadProducts() {
        products = ProductBUS.getInstance().getAllProducts();
        if(products != null) {
            for(ProductDTO p : products) {
                cbProduct.addItem(p.getProductCode() + " - " + p.getName());
            }
        }
    }

    private void updateTable() {
        modelCart.setRowCount(0);
        long total = 0;
        for(ReceiptItemDTO i : cart) {
            long sum = (long)i.getQuantity() * i.getUnitPrice();
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
        lbl.setForeground(new Color(100, 100, 100));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void styleControl(JComponent c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setPreferredSize(new Dimension(100, 35));
        c.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 8, 5, 8)
        ));
        if(c instanceof JComboBox) c.setBackground(Color.WHITE);
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setFillsViewportHeight(true);

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

    public boolean isSuccess() { return isSuccess; }
}