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
    // Giữ nguyên các biến logic
    private JComboBox<String> cbProduct;
    private JTextField txtQty, txtCustomer;
    private DefaultTableModel modelCart, modelHistory;
    private JTable tableCart, tableHistory; // Thêm biến table để style
    private List<ReceiptItemDTO> cart = new ArrayList<>();
    private List<ProductDTO> products;
    private DecimalFormat df = new DecimalFormat("###,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm"); // Cập nhật format ngày giờ cho đẹp

    // Màu sắc chủ đạo từ hình ảnh
    private Color colorBackground = new Color(240, 242, 245);
    private Color colorBlue = new Color(66, 133, 244);
    private Color colorGreen = new Color(22, 160, 133);
    private Color colorText = new Color(50, 50, 50);

    public ExportGUI() {
        setLayout(new BorderLayout());
        setBackground(colorBackground);
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding bao quanh toàn màn hình

        // --- MAIN CONTAINER (Chia 2 cột) ---
        JPanel pMain = new JPanel(new GridLayout(1, 2, 20, 0)); // 2 cột, khoảng cách 20px
        pMain.setOpaque(false);
        add(pMain, BorderLayout.CENTER);

        // =========================================================================
        // === LEFT PANEL: TẠO PHIẾU XUẤT KHO ===
        // =========================================================================
        JPanel pLeft = new JPanel(new BorderLayout(10, 10));
        pLeft.setBackground(Color.WHITE);
        pLeft.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding trong thẻ trắng

        // 1. Header Left
        JLabel lblTitleLeft = new JLabel("TẠO PHIẾU XUẤT KHO");
        lblTitleLeft.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitleLeft.setForeground(new Color(44, 62, 80));

        // 2. Form Inputs (Sử dụng Box Layout để xếp chồng dọc)
        JPanel pForm = new JPanel();
        pForm.setLayout(new BoxLayout(pForm, BoxLayout.Y_AXIS));
        pForm.setBackground(Color.WHITE);

        cbProduct = new JComboBox<>();
        txtQty = new JTextField();
        txtCustomer = new JTextField();
        
        // Style inputs
        styleControl(cbProduct);
        styleControl(txtQty);
        styleControl(txtCustomer);

        // Thêm các thành phần vào Form
        pForm.add(createInputGroup("Sản Phẩm:", cbProduct));
        pForm.add(Box.createVerticalStrut(10));
        pForm.add(createInputGroup("Số Lượng Bán:", txtQty));
        pForm.add(Box.createVerticalStrut(10));
        pForm.add(createInputGroup("Tên Khách Hàng:", txtCustomer));
        pForm.add(Box.createVerticalStrut(15));

        // Nút Thêm vào giỏ
        JButton btnAdd = new JButton("Thêm vào giỏ");
        styleButton(btnAdd, colorBlue);
        btnAdd.setPreferredSize(new Dimension(100, 35));
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Full width
        pForm.add(btnAdd);

        // 3. Cart Table (Nằm giữa form và nút thanh toán)
        modelCart = new DefaultTableModel(new String[]{"Sản phẩm", "SL", "Đơn giá", "Thành tiền"}, 0);
        tableCart = new JTable(modelCart);
        styleTable(tableCart);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.getViewport().setBackground(Color.WHITE);
        scrollCart.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));
        
        // Panel chứa phần Form và Table Cart
        JPanel pLeftCenter = new JPanel(new BorderLayout(0, 15));
        pLeftCenter.setBackground(Color.WHITE);
        pLeftCenter.add(pForm, BorderLayout.NORTH);
        pLeftCenter.add(scrollCart, BorderLayout.CENTER);

        // 4. Button Thanh Toán (To, Xanh lá, ở dưới cùng)
        JButton btnPay = new JButton("THANH TOÁN & XUẤT KHO");
        styleButton(btnPay, colorGreen);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPay.setPreferredSize(new Dimension(100, 50));

        // Ráp pLeft
        pLeft.add(lblTitleLeft, BorderLayout.NORTH);
        pLeft.add(pLeftCenter, BorderLayout.CENTER);
        pLeft.add(btnPay, BorderLayout.SOUTH);

        // =========================================================================
        // === RIGHT PANEL: LỊCH SỬ XUẤT KHO ===
        // =========================================================================
        JPanel pRight = new JPanel(new BorderLayout(10, 10));
        pRight.setBackground(Color.WHITE);
        pRight.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header Right (Title + Button Refresh)
        JPanel pRightHeader = new JPanel(new BorderLayout());
        pRightHeader.setBackground(Color.WHITE);
        
        JLabel lblTitleRight = new JLabel("LỊCH SỬ XUẤT KHO");
        lblTitleRight.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitleRight.setForeground(new Color(44, 62, 80));
        
        JButton btnRefresh = new JButton("Làm mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        btnRefresh.setPreferredSize(new Dimension(90, 30));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pRightHeader.add(lblTitleRight, BorderLayout.WEST);
        pRightHeader.add(btnRefresh, BorderLayout.EAST);

        // 2. History Table
        modelHistory = new DefaultTableModel(new String[]{"Mã HĐ", "Khách Hàng", "Ngày Xuất", "Tổng Tiền"}, 0);
        tableHistory = new JTable(modelHistory);
        styleTable(tableHistory);
        JScrollPane scrollHistory = new JScrollPane(tableHistory);
        scrollHistory.getViewport().setBackground(Color.WHITE);
        scrollHistory.setBorder(BorderFactory.createEmptyBorder()); // Bỏ viền scrollpane cho đẹp

        // Ráp pRight
        pRight.add(pRightHeader, BorderLayout.NORTH);
        pRight.add(scrollHistory, BorderLayout.CENTER);

        // --- Add Left & Right to Main ---
        pMain.add(pLeft);
        pMain.add(pRight);

        // =========================================================================
        // === LOGIC & EVENT HANDLERS (GIỮ NGUYÊN) ===
        // =========================================================================
        loadData();

        btnAdd.addActionListener(e -> {
            try {
                int idx = cbProduct.getSelectedIndex();
                if(idx < 0) return;
                // Parse Product string to get real object (logic cũ hơi lỏng lẻo khi dựa vào index, 
                // nhưng mình giữ nguyên theo yêu cầu của bạn)
                if(products == null || idx >= products.size()) return;
                
                ProductDTO p = products.get(idx);
                String qtyText = txtQty.getText();
                if(qtyText.isEmpty()) { JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!"); return; }
                
                int qty = Integer.parseInt(qtyText);

                if(qty > p.getQuantity()) { 
                    JOptionPane.showMessageDialog(this, "Tồn kho không đủ! (Còn " + p.getQuantity() + ")"); 
                    return; 
                }

                // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa để cộng dồn (Option cải tiến nhỏ)
                boolean exists = false;
//                for(ReceiptItemDTO item : cart) {
//                    if(item.getProduct_id() == p.getProductCode()) {
//                         // Logic cũ của bạn add dòng mới, nếu muốn giữ nguyên logic cũ thì bỏ qua đoạn check này
//                    }
//                }

                cart.add(new ReceiptItemDTO(p.getProductCode(), p.getName(), qty, p.getPrice()));
                updateCart();
            } catch (NumberFormatException ex) { 
                JOptionPane.showMessageDialog(this, "Số lượng phải là số!"); 
            } catch (Exception ex) { 
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage()); 
            }
        });

        btnPay.addActionListener(e -> {
            if(cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
                return;
            }
            if(txtCustomer.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
                return;
            }
            
            boolean ok = ExportBUS.getInstance().addNewExport(txtCustomer.getText(), "Xuất bán", cart);
            if(ok) {
                JOptionPane.showMessageDialog(this, "Xuất kho thành công!");
                cart.clear(); 
                txtQty.setText("");
                txtCustomer.setText("");
                updateCart(); 
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi! Có thể hàng vừa hết hoặc lỗi hệ thống.");
            }
        });
        
        btnRefresh.addActionListener(e -> loadData());
    }

    // =========================================================================
    // === HELPER METHODS FOR UI STYLING ===
    // =========================================================================

    // Tạo một nhóm Label + Input xếp chồng
    private JPanel createInputGroup(String title, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Giới hạn chiều cao
        
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    // Style cho các ô nhập liệu
    private void styleControl(JComponent c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if(c instanceof JTextField || c instanceof JComboBox) {
            c.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1), 
                new EmptyBorder(5, 8, 5, 8) // Padding text bên trong
            ));
        }
    }

    // Style cho Button
    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Style cho Table để giống hình (Header trắng, row cao)
    private void styleTable(JTable table) {
        table.setRowHeight(35); // Chiều cao dòng lớn hơn
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false); // Bỏ kẻ dọc
        table.setGridColor(new Color(230, 230, 230));
        
        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 247, 250)); // Màu nền header xám nhạt
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        
        // Căn giữa hoặc trái cho cột (Tùy chọn)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // Áp dụng renderer nếu cần thiết cho từng cột cụ thể...
    }

    // =========================================================================
    // === DATA METHODS (GIỮ NGUYÊN) ===
    // =========================================================================

    private void updateCart() {
        modelCart.setRowCount(0);
        long grandTotal = 0;
        for(ReceiptItemDTO i : cart) {
            long total = (long)i.getQuantity() * i.getUnitPrice();
            grandTotal += total;
            modelCart.addRow(new Object[]{
                i.getName(), 
                i.getQuantity(), 
                df.format(i.getUnitPrice()), 
                df.format(total)
            });
        }
    }

    public void loadData() {
        cbProduct.removeAllItems();
        products = ProductBUS.getInstance().getAllProducts();
        if(products != null) {
            for(ProductDTO p : products) {
                cbProduct.addItem(p.getName() + " (Tồn: " + p.getQuantity() + ")");
            }
        }
        
        modelHistory.setRowCount(0);
        List<ExportDTO> history = ExportBUS.getInstance().getAllExports();
        if(history != null) {
            // Đảo ngược list để hiện cái mới nhất lên đầu (nếu cần)
            // Collections.reverse(history); 
            for(ExportDTO e : history) {
                modelHistory.addRow(new Object[]{
                    e.get_id(), 
                    e.getCustomerCode(), 
                    sdf.format(e.getExportDate()), 
                    df.format(e.getTotalPrice())
                });
            }
        }
    }
}