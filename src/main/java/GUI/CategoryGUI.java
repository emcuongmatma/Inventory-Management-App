package GUI;

import BUS.CategoryBUS;
import DTO.CategoryDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CategoryGUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtSearch;

    // Màu sắc chủ đạo (Đồng bộ với hệ thống)
    private final Color COLOR_BG = new Color(240, 242, 245);
    private final Color COLOR_PRIMARY = new Color(66, 133, 244);
    private final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private final Color COLOR_DANGER = new Color(231, 76, 60);
    private final Color COLOR_GRAY = new Color(149, 165, 166);

    public CategoryGUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding bao quanh

        // --- MAIN CARD ---
        JPanel pnlCard = new JPanel(new BorderLayout(0, 10));
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // =========================================================================
        // === HEADER & INPUT AREA ===
        // =========================================================================
        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));
        pnlHeader.setBackground(Color.WHITE);

        // 1. Title
        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2. Input Section (Thêm mới)
        JPanel pnlInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "Thêm Danh Mục Mới",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 100, 100)
        ));
        pnlInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        pnlInput.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtName = new JTextField(25);
        styleControl(txtName); // Style đẹp cho input
        JButton btnAdd = createBtn("Thêm Ngay", COLOR_SUCCESS);

        pnlInput.add(new JLabel("Tên Danh Mục:"));
        pnlInput.add(txtName);
        pnlInput.add(btnAdd);

        // 3. Toolbar (Actions + Search)
        JPanel pnlToolbar = new JPanel(new BorderLayout());
        pnlToolbar.setBackground(Color.WHITE);
        pnlToolbar.setBorder(new EmptyBorder(15, 0, 0, 0)); // Padding top
        pnlToolbar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Left: Actions
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlActions.setBackground(Color.WHITE);
        JButton btnDelete = createBtn("Xóa Chọn", COLOR_DANGER);
        JButton btnRefresh = createBtn("Làm Mới", COLOR_GRAY);
        pnlActions.add(btnDelete);
        pnlActions.add(btnRefresh);

        // Right: Search
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSearch.setBackground(Color.WHITE);
        
        txtSearch = new JTextField(20);
        styleControl(txtSearch);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm mã hoặc tên...");
        JButton btnSearch = createBtn("Tìm Kiếm", COLOR_PRIMARY);
        
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        pnlToolbar.add(pnlActions, BorderLayout.WEST);
        pnlToolbar.add(pnlSearch, BorderLayout.EAST);

        // Add to Header
        pnlHeader.add(lblTitle);
        pnlHeader.add(Box.createVerticalStrut(15));
        pnlHeader.add(pnlInput);
        pnlHeader.add(pnlToolbar);
        pnlHeader.add(Box.createVerticalStrut(15));

        // =========================================================================
        // === TABLE AREA ===
        // =========================================================================
        String[] headers = {"STT", "Mã Danh Mục", "Tên Danh Mục"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        styleTable(table); // Style bảng đẹp

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        pnlCard.add(pnlHeader, BorderLayout.NORTH);
        pnlCard.add(scrollPane, BorderLayout.CENTER);

        add(pnlCard, BorderLayout.CENTER);

        // =========================================================================
        // === EVENTS (LOGIC GIỮ NGUYÊN) ===
        // =========================================================================
        
        // Sự kiện Nút Thêm
        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập tên danh mục!");
                return;
            }
            
            try {
                // Giả sử BUS trả về boolean hoặc void như code cũ của bạn
                // Nếu BUS.addNewCategory trả về void, đoạn code này chạy bình thường
                // Nếu BUS trả về boolean, bạn có thể thêm if check
                CategoryBUS.getInstance().addNewCategory(name); 
                
                JOptionPane.showMessageDialog(this, "Thao tác thành công!");
                loadData();
                txtName.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage());
            }
        });

        // Sự kiện Nút Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
                return;
            }
            
            String code = model.getValueAt(row, 1).toString();
            String name = model.getValueAt(row, 2).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa danh mục: " + name + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                CategoryBUS.getInstance().deleteCategory(code);
                JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                loadData();
            }
        });

        // Sự kiện Nút Tìm kiếm
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            List<CategoryDTO> list = CategoryBUS.getInstance().findCategoryByCodeOrName(keyword);
            updateTable(list);
        });

        // Sự kiện Nút Làm mới
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            txtName.setText("");
            loadData();
        });

        loadData();
    }

    // =========================================================================
    // === DATA METHODS ===
    // =========================================================================
    
    public void loadData() {
        List<CategoryDTO> list = CategoryBUS.getInstance().getAllCategory();
        updateTable(list);
    }

    private void updateTable(List<CategoryDTO> list) {
        model.setRowCount(0);
        int stt = 1;
        if (list != null) {
            for (CategoryDTO c : list) {
                model.addRow(new Object[]{
                    stt++,
                    c.getCategoryCode(),
                    c.getName()
                });
            }
        }
    }

    // =========================================================================
    // === HELPER METHODS FOR STYLING ===
    // =========================================================================

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleControl(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        
        // Căn giữa STT và Mã
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setMaxWidth(60); 
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setMaxWidth(150);
    }
}