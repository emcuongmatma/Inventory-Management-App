package GUI;

import BUS.BrandBUS;
import BUS.CategoryBUS;
import BUS.ProductBUS;
import DTO.BrandDTO;
import DTO.CategoryDTO;
import DTO.ProductDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductGUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JComboBox<BrandItem> cbbFilterBrand;
    private JComboBox<CategoryItem> cbbFilterCategory;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnSearch;

    private final DecimalFormat df = new DecimalFormat("###,###");
    private final Map<String, String> brandMap = new HashMap<>();
    private final Map<String, String> categoryMap = new HashMap<>();

    private static final Color COLOR_BG = new Color(240, 242, 245);
    private static final Color COLOR_PRIMARY = new Color(66, 133, 244);
    private static final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private static final Color COLOR_WARNING = new Color(241, 196, 15);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);
    private static final Color COLOR_GRAY = new Color(149, 165, 166);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    public ProductGUI() {
        initUI();
        initEvents();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlCard = new JPanel(new BorderLayout(0, 15));
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        pnlCard.add(createHeader(), BorderLayout.NORTH);
        pnlCard.add(createTableSection(), BorderLayout.CENTER);

        add(pnlCard, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM & KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        pnlHeader.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlToolbar = new JPanel(new BorderLayout());
        pnlToolbar.setBackground(Color.WHITE);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pnlButtons.setBackground(Color.WHITE);

        btnAdd = createBtn("Thêm", COLOR_SUCCESS);
        btnEdit = createBtn("Sửa / Xem", COLOR_WARNING);
        btnDelete = createBtn("Xóa SP", COLOR_DANGER);
        btnRefresh = createBtn("Làm Mới", COLOR_GRAY);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnEdit);
        pnlButtons.add(btnDelete);
        pnlButtons.add(btnRefresh);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearch.setBackground(Color.WHITE);

        cbbFilterBrand = new JComboBox<>();
        styleComboBox(cbbFilterBrand);

        cbbFilterCategory = new JComboBox<>();
        styleComboBox(cbbFilterCategory);

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm tên/mã...");
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));

        btnSearch = createBtn("Tìm", COLOR_PRIMARY);
        btnSearch.setPreferredSize(new Dimension(80, 32));

        pnlSearch.add(cbbFilterBrand);
        pnlSearch.add(cbbFilterCategory);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        pnlToolbar.add(pnlButtons, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);
        pnlHeader.add(pnlToolbar, BorderLayout.CENTER);

        return pnlHeader;
    }

    private JScrollPane createTableSection() {
        String[] headers = {"Mã SP", "Tên SP", "Tên Thương Hiệu", "Tên Danh Mục", "Tồn Kho", "Đơn Giá"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        return scrollPane;
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            ProductDialog dlg = new ProductDialog(parent, "Nhập Sản Phẩm Mới", null);
            dlg.setVisible(true);
            if (dlg.isSuccess()) loadData();
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
                return;
            }
            try {
                String pCode = model.getValueAt(row, 0).toString();
                List<ProductDTO> list = ProductBUS.getInstance().getByCodeOrName(pCode);
                if (list != null && !list.isEmpty()) {
                    ProductDTO original = list.get(0);
                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                    ProductDialog dlg = new ProductDialog(parent, "Cập Nhật Sản Phẩm", original);
                    dlg.setVisible(true);
                    if (dlg.isSuccess()) loadData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            String code = model.getValueAt(row, 0).toString();
            String name = model.getValueAt(row, 1).toString();
            int cf = JOptionPane.showConfirmDialog(this, "Xóa SP: " + name + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                ProductBUS.getInstance().deleteProduct(code);
                loadData();
            }
        });

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            cbbFilterBrand.setSelectedIndex(0);
            cbbFilterCategory.setSelectedIndex(0);
            loadData();
        });

        btnSearch.addActionListener(e -> performSearch());

        cbbFilterBrand.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) performSearch();
        });

        cbbFilterCategory.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) performSearch();
        });
    }

    public void loadData() {
        loadComboboxData();
        model.setRowCount(0);
        for (ProductDTO p : ProductBUS.getInstance().getAllProducts()) addRow(p);
    }

    private void loadComboboxData() {
        cbbFilterBrand.removeAllItems();
        cbbFilterCategory.removeAllItems();
        brandMap.clear();
        categoryMap.clear();

        cbbFilterBrand.addItem(new BrandItem("", "Tất cả Thương hiệu"));
        cbbFilterCategory.addItem(new CategoryItem("", "Tất cả Danh mục"));

        List<BrandDTO> brands = BrandBUS.getInstance().getAllBrand();
        if (brands != null) {
            for (BrandDTO b : brands) {
                cbbFilterBrand.addItem(new BrandItem(b.getBrandCode(), b.getName()));
                brandMap.put(b.getBrandCode(), b.getName());
            }
        }

        List<CategoryDTO> cats = CategoryBUS.getInstance().getAllCategory();
        if (cats != null) {
            for (CategoryDTO c : cats) {
                cbbFilterCategory.addItem(new CategoryItem(c.getCategoryCode(), c.getName()));
                categoryMap.put(c.getCategoryCode(), c.getName());
            }
        }
    }

    private void performSearch() {
        String key = txtSearch.getText().trim();
        BrandItem bItem = (BrandItem) cbbFilterBrand.getSelectedItem();
        CategoryItem cItem = (CategoryItem) cbbFilterCategory.getSelectedItem();

        String brandCode = (bItem != null) ? bItem.code : "";
        String catCode = (cItem != null) ? cItem.code : "";

        model.setRowCount(0);
        List<ProductDTO> list;

        if (!key.isEmpty()) {
            list = ProductBUS.getInstance().getByCodeOrName(key);
        } else if (!brandCode.isEmpty()) {
            list = ProductBUS.getInstance().getByBrandCode(brandCode);
        } else if (!catCode.isEmpty()) {
            list = ProductBUS.getInstance().getByCategory(catCode);
        } else {
            list = ProductBUS.getInstance().getAllProducts();
        }

        if (list != null) for (ProductDTO p : list) addRow(p);
    }

    private void addRow(ProductDTO p) {
        String brandName = brandMap.getOrDefault(p.getBrandCode(), p.getBrandCode());
        String catName = categoryMap.getOrDefault(p.getCategoryCode(), p.getCategoryCode());

        model.addRow(new Object[]{
            p.getProductCode(),
            p.getName(),
            brandName,
            catName,
            p.getQuantity(),
            df.format(p.getPrice())
        });
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 32));
        return btn;
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(FONT_PLAIN);
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(150, 32));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);
         table.setShowVerticalLines(true); 
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(center);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0, 0, 0, 10));
        table.getColumnModel().getColumn(5).setCellRenderer(right);
    }

    private static class BrandItem {
        String code, name;
        public BrandItem(String code, String name) { this.code = code; this.name = name; }
        @Override public String toString() { return name; }
    }

    private static class CategoryItem {
        String code, name;
        public CategoryItem(String code, String name) { this.code = code; this.name = name; }
        @Override public String toString() { return name; }
    }
}