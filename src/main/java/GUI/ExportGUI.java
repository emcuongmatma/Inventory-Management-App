package GUI;

import BUS.CustomerBUS;
import BUS.ExportBUS;
import BUS.ProductBUS;
import DAO.CustomerDAO;
import DTO.CustomerDTO;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExportGUI extends JPanel {

    private JComboBox<String> cbProduct;
    private JTextField txtQty;
    private JTextField txtCusName, txtCusPhone, txtCusAddress;
    
    private DefaultTableModel modelCart, modelHistory;
    private JTable tableCart, tableHistory;
    private JButton btnAdd, btnPay, btnRefresh;
    
    private List<ReceiptItemDTO> cart = new ArrayList<>();
    private List<ProductDTO> products;
    private List<ExportDTO> exportList; 
    
    private final DecimalFormat df = new DecimalFormat("###,###");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final Color COLOR_BG = new Color(240, 242, 245);
    private static final Color COLOR_BLUE = new Color(66, 133, 244);
    private static final Color COLOR_GREEN = new Color(22, 160, 133);

    public ExportGUI() {
        initUI();
        initEvents();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createLeftPanel(), createRightPanel());
        splitPane.setResizeWeight(0.6);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createLeftPanel() {
        JPanel pLeft = new JPanel(new BorderLayout(0, 10));
        pLeft.setBackground(Color.WHITE);
        pLeft.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel("TẠO PHIẾU XUẤT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));

        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        cbProduct = new JComboBox<>();
        styleControl(cbProduct);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        pForm.add(createInputGroup("Chọn Sản Phẩm:", cbProduct), gbc);

        txtQty = new JTextField();
        styleControl(txtQty);
        btnAdd = new JButton("Thêm Vào Giỏ");
        styleButton(btnAdd, COLOR_BLUE);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5;
        pForm.add(createInputGroup("Số Lượng:", txtQty), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1; 
        JPanel pBtnWrapper = new JPanel(new BorderLayout());
        pBtnWrapper.setBackground(Color.WHITE);
        pBtnWrapper.setBorder(new EmptyBorder(18, 0, 0, 0));
        pBtnWrapper.add(btnAdd);
        pForm.add(pBtnWrapper, gbc);

        JPanel pCusGroup = new JPanel(new GridLayout(1, 2, 10, 10));
        pCusGroup.setBackground(Color.WHITE);
        
        txtCusPhone = new JTextField();
        txtCusName = new JTextField();
        txtCusAddress = new JTextField();
        styleControl(txtCusPhone); styleControl(txtCusName); styleControl(txtCusAddress);

        pCusGroup.add(createInputGroup("Số Điện Thoại (*):", txtCusPhone));
        pCusGroup.add(createInputGroup("Tên Khách Hàng (*):", txtCusName));
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; 
        pForm.add(pCusGroup, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        pForm.add(createInputGroup("Địa Chỉ Giao Hàng (*):", txtCusAddress), gbc);

        modelCart = new DefaultTableModel(new String[]{"Sản phẩm", "SL", "Đơn giá", "Thành tiền"}, 0);
        tableCart = new JTable(modelCart);
        styleTable(tableCart);
        JScrollPane scrollCart = new JScrollPane(tableCart);
        scrollCart.getViewport().setBackground(Color.WHITE);

        btnPay = new JButton("THANH TOÁN & XUẤT KHO");
        styleButton(btnPay, COLOR_GREEN);
        btnPay.setPreferredSize(new Dimension(100, 45));

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.setBackground(Color.WHITE);
        pTop.add(lblTitle, BorderLayout.NORTH);
        pTop.add(pForm, BorderLayout.CENTER);

        pLeft.add(pTop, BorderLayout.NORTH);
        pLeft.add(scrollCart, BorderLayout.CENTER);
        pLeft.add(btnPay, BorderLayout.SOUTH);

        return pLeft;
    }

    private JPanel createRightPanel() {
        JPanel pRight = new JPanel(new BorderLayout(10, 10));
        pRight.setBackground(Color.WHITE);
        pRight.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("LỊCH SỬ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(44, 62, 80));

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, new Color(108, 117, 125));
        btnRefresh.setPreferredSize(new Dimension(80, 28));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 11));

        pHeader.add(lblTitle, BorderLayout.WEST);
        pHeader.add(btnRefresh, BorderLayout.EAST);

        modelHistory = new DefaultTableModel(new String[]{"Mã HĐ", "Khách Hàng", "Ngày", "Tổng"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableHistory = new JTable(modelHistory);
        styleTable(tableHistory);
        
        tableHistory.getColumnModel().getColumn(0).setPreferredWidth(50);
        
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

        tableHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2 && tableHistory.getSelectedRow() != -1) {
                    int row = tableHistory.getSelectedRow();
                    ExportDTO selectedExport = exportList.get(row);
                    
                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(ExportGUI.this);
                    new ExportDetailDialog(parent, selectedExport).setVisible(true);
                }
            }
        });
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

            boolean exists = false;
            for(ReceiptItemDTO item : cart) {
                if(item.getProductCode().equals(p.getProductCode())) {
                    item.setQuantity(item.getQuantity() + qty);
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                cart.add(new ReceiptItemDTO(p.getProductCode(), p.getName(), qty, p.getPrice()));
            }

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

        String name = txtCusName.getText().trim();
        String phone = txtCusPhone.getText().trim();
        String address = txtCusAddress.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ: Tên, SĐT và Địa chỉ khách hàng!");
            return;
        }

        try {
            CustomerDAO cusDAO = new CustomerDAO();
            List<CustomerDTO> existing = cusDAO.findByPhone(phone);
            if(existing.isEmpty()) {
                CustomerDTO newCus = new CustomerDTO();
                newCus.setName(name);
                newCus.setPhone(phone);
                newCus.setAddress(address);
                cusDAO.insert(newCus);
            }

            boolean ok = ExportBUS.getInstance().addNewExport(phone, "Xuất bán cho " + name, cart);
            
            if (ok) {
                JOptionPane.showMessageDialog(this, "Xuất kho thành công!");
                cart.clear();
                updateCart();
                loadData();
                txtQty.setText("");
                txtCusName.setText("");
                txtCusPhone.setText("");
                txtCusAddress.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi! Có thể hàng vừa hết hoặc lỗi hệ thống.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thanh toán: " + e.getMessage());
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
        exportList = ExportBUS.getInstance().getAllExports();
        if (exportList != null) {
            for (ExportDTO e : exportList) {
                String phoneCode = e.getCustomerCode();
                String displayName = phoneCode;
                
                List<CustomerDTO> customers = CustomerBUS.getInstance().findByPhone(phoneCode);
                if (customers != null && !customers.isEmpty()) {
                    displayName = customers.get(0).getName();
                }

                modelHistory.addRow(new Object[]{
                    e.get_id(),
                    displayName,
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
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(100, 100, 100));
        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private void styleControl(JComponent c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (c instanceof JTextField || c instanceof JComboBox) {
            c.setPreferredSize(new Dimension(100, 30));
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.setShowVerticalLines(true); 
        table.setShowHorizontalLines(true); 
        table.setGridColor(new Color(230, 230, 230));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        if(table.getColumnCount() > 1)
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
    }
}