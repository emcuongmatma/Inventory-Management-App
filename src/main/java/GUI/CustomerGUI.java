package GUI;

import BUS.CustomerBUS;
import DTO.CustomerDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerGUI extends JPanel {

    private JTextField txtName, txtPhone, txtAddress, txtSearch;
    private JTable table;
    private DefaultTableModel model;

    public CustomerGUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(10, 10, 10, 10));


        // --- 2. RIGHT PANEL (DANH SÁCH & TÌM KIẾM) ---
        JPanel pnlRight = new JPanel(new BorderLayout(0, 10));
        pnlRight.setOpaque(false);

        // Toolbar tìm kiếm
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        txtSearch = new JTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập số điện thoại tìm kiếm...");
        JButton btnSearch = createButton("Tìm Kiếm", new Color(52, 152, 219));
        // Giả sử bạn muốn nút reload để hiện lại tất cả (cần bổ sung getAll vào BUS/DAO nếu chưa có)
        JButton btnReload = createButton("Làm Mới", new Color(149, 165, 166));

        pnlSearch.add(new JLabel("Tìm SĐT: "));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnReload);
        
        pnlRight.add(pnlSearch, BorderLayout.NORTH);

        // Table
        String[] headers = {"STT", "Họ Tên", "Số Điện Thoại", "Địa Chỉ"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        
        // Style Table
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(25, 42, 86));
        table.getTableHeader().setForeground(Color.WHITE);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // SĐT căn giữa
        table.getColumnModel().getColumn(0).setMaxWidth(50); // Cột STT nhỏ

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        pnlRight.add(scrollPane, BorderLayout.CENTER);

        add(pnlRight, BorderLayout.CENTER);

        // --- 3. EVENTS ---

        btnSearch.addActionListener(e -> {
            String phone = txtSearch.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập SĐT!");
                return;
            }
            List<CustomerDTO> list = CustomerBUS.getInstance().findByPhone(phone);
            updateTable(list);
        });

        // Nút Reload (Clear tìm kiếm)
        btnReload.addActionListener(e -> {
            txtSearch.setText("");
            model.setRowCount(0); // Clear bảng hoặc load lại toàn bộ nếu có getAll
        });
    }

    private void updateTable(List<CustomerDTO> list) {
        model.setRowCount(0);
        int stt = 1;
        if (list != null) {
            for (CustomerDTO c : list) {
                model.addRow(new Object[]{
                    stt++,
                    c.getName(),
                    c.getPhone(),
                    c.getAddress()
                });
            }
        }
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}