package GUI;

import BUS.BrandBUS;
import BUS.CategoryBUS;
import BUS.ProductBUS;
import DTO.BrandDTO;
import DTO.CategoryDTO;
import DTO.ProductDTO;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class ProductDialog extends JDialog {
    private JTextField txtCode, txtName, txtPrice;
    // Bỏ JTextField txtQty
    
    private JComboBox<BrandItem> cbBrand;
    private JComboBox<CategoryItem> cbCategory;
    
    private boolean isSuccess = false;
    private ProductDTO productEdit;

    // Màu sắc chủ đạo
    private final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private final Color COLOR_DANGER = new Color(231, 76, 60);

    public ProductDialog(JFrame parent, String title, ProductDTO product) {
        super(parent, title, true);
        this.productEdit = product;
        setSize(450, 500); // Giảm chiều cao vì bớt 1 trường
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- TITLE ---
        JLabel lblTitle = new JLabel(title.toUpperCase(), JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- FORM PANEL ---
        JPanel pForm = new JPanel();
        pForm.setLayout(new BoxLayout(pForm, BoxLayout.Y_AXIS));
        pForm.setBackground(Color.WHITE);
        pForm.setBorder(new EmptyBorder(10, 40, 20, 40));

        // Init Components
        txtCode = new JTextField(); 
        txtCode.setEditable(false);
        txtCode.setBackground(new Color(245, 245, 245));
        
        txtName = new JTextField();
        txtPrice = new JTextField();
        // txtQty = new JTextField(); // Xóa
        
        cbBrand = new JComboBox<>();
        cbCategory = new JComboBox<>();
        
        // Style Components
        styleControl(txtCode);
        styleControl(txtName);
        styleControl(txtPrice);
        styleControl(cbBrand);
        styleControl(cbCategory);

        // --- LOAD DATA COMBOBOX ---
        List<BrandDTO> brands = BrandBUS.getInstance().getAllBrand();
        if(brands != null) {
            for(BrandDTO b : brands) cbBrand.addItem(new BrandItem(b.getBrandCode(), b.getName()));
        }
        
        List<CategoryDTO> cats = CategoryBUS.getInstance().getAllCategory();
        if(cats != null) {
            for(CategoryDTO c : cats) cbCategory.addItem(new CategoryItem(c.getCategoryCode(), c.getName()));
        }

        // --- SET VALUES ---
        if(product != null) {
            txtCode.setText(product.getProductCode());
            txtName.setText(product.getName());
            txtPrice.setText(String.valueOf(product.getPrice()));
            // Không set số lượng lên form nữa
            
            setSelectedBrand(product.getBrandCode());
            setSelectedCategory(product.getCategoryCode());
            
        } else {
            txtCode.setText("Tự động tạo");
            txtCode.setForeground(Color.GRAY);
            txtCode.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        }

        // Add to Panel
        pForm.add(createFormGroup("Mã Sản Phẩm:", txtCode));
        pForm.add(Box.createVerticalStrut(10));
        pForm.add(createFormGroup("Tên Sản Phẩm:", txtName));
        pForm.add(Box.createVerticalStrut(10));
        
        JPanel pRow = new JPanel(new GridLayout(1, 2, 15, 0));
        pRow.setBackground(Color.WHITE);
        pRow.add(createFormGroup("Thương Hiệu:", cbBrand));
        pRow.add(createFormGroup("Danh Mục:", cbCategory));
        pForm.add(pRow);
        
        pForm.add(Box.createVerticalStrut(10));
        pForm.add(createFormGroup("Đơn Giá Niêm Yết (VNĐ):", txtPrice));
        // pForm.add(createFormGroup("Số Lượng:", txtQty)); // Bỏ dòng này

        add(pForm, BorderLayout.CENTER);

        // --- BUTTON PANEL ---
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pBtn.setBackground(new Color(245, 247, 250));
        pBtn.setBorder(new EmptyBorder(5, 0, 5, 20));

        JButton btnSave = createBtn("Lưu Lại", COLOR_SUCCESS);
        JButton btnCancel = createBtn("Hủy Bỏ", COLOR_DANGER);
        
        pBtn.add(btnSave);
        pBtn.add(btnCancel);
        add(pBtn, BorderLayout.SOUTH);

        // --- EVENTS ---
        btnSave.addActionListener(e -> {
            try {
                if(txtName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tên không được trống"); return;
                }
                if(cbBrand.getSelectedItem() == null || cbCategory.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn Thương Hiệu/Danh Mục!"); return;
                }

                ProductDTO dto = new ProductDTO();
                dto.setProductCode(productEdit != null ? txtCode.getText() : null);
                dto.setName(txtName.getText());
                
                BrandItem selectedBrand = (BrandItem) cbBrand.getSelectedItem();
                CategoryItem selectedCat = (CategoryItem) cbCategory.getSelectedItem();
                
                dto.setBrandCode(selectedBrand.code);
                dto.setCategoryCode(selectedCat.code);
                dto.setPrice(Integer.parseInt(txtPrice.getText()));
                
                // Set mặc định số lượng
                if(productEdit != null) {
                    dto.setQuantity(productEdit.getQuantity()); // Giữ nguyên số lượng cũ nếu đang sửa
                } else {
                    dto.setQuantity(0); // Mới tạo thì bằng 0
                }
                
                boolean ok = (productEdit == null) ? ProductBUS.getInstance().addProduct(dto) : true;
                if(productEdit != null) ProductBUS.getInstance().updateProduct(dto);
                
                if(ok) {
                    isSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thao tác thất bại!");
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá phải là số!");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        
        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isSuccess() { return isSuccess; }

    // Helper Classes & Methods (Giữ nguyên)
    class BrandItem {
        String code, name;
        public BrandItem(String code, String name) { this.code = code; this.name = name; }
        @Override public String toString() { return name; }
    }

    class CategoryItem {
        String code, name;
        public CategoryItem(String code, String name) { this.code = code; this.name = name; }
        @Override public String toString() { return name; }
    }

    private void setSelectedBrand(String code) {
        for(int i=0; i<cbBrand.getItemCount(); i++) {
            if(cbBrand.getItemAt(i).code.equals(code)) { cbBrand.setSelectedIndex(i); break; }
        }
    }

    private void setSelectedCategory(String code) {
        for(int i=0; i<cbCategory.getItemCount(); i++) {
            if(cbCategory.getItemAt(i).code.equals(code)) { cbCategory.setSelectedIndex(i); break; }
        }
    }

    private JPanel createFormGroup(String title, JComponent component) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));
        p.add(lbl, BorderLayout.NORTH);
        p.add(component, BorderLayout.CENTER);
        return p;
    }

    private void styleControl(JComponent c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setPreferredSize(new Dimension(100, 35));
        c.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 8, 5, 8)));
        if(c instanceof JComboBox) ((JComboBox<?>)c).setBackground(Color.WHITE);
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}