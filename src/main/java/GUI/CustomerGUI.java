package GUI;

import BUS.CustomerBUS;
import DTO.CustomerDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CustomerGUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnSearch, btnReload;

    private static final Color COLOR_BG = new Color(240, 242, 245);
    private static final Color COLOR_PRIMARY = new Color(66, 133, 244);
    private static final Color COLOR_GRAY = new Color(149, 165, 166);
    private static final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);

    public CustomerGUI() {
        initUI();
        initEvents();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlMain = new JPanel(new BorderLayout(0, 15));
        pnlMain.setBackground(COLOR_BG);

        pnlMain.add(createSearchPanel(), BorderLayout.NORTH);
        pnlMain.add(createTableSection(), BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(new LineBorder(new Color(220, 220, 220)));

        JLabel lblSearch = new JLabel("Tìm kiếm SĐT:");
        lblSearch.setFont(FONT_BOLD);

        txtSearch = new JTextField(25);
        styleControl(txtSearch);
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập số điện thoại khách hàng...");

        btnSearch = createBtn("Tìm Kiếm", COLOR_PRIMARY);
        btnReload = createBtn("Làm Mới", COLOR_GRAY);

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnReload);

        return pnlSearch;
    }

    private JScrollPane createTableSection() {
        String[] headers = {"STT", "Họ Tên", "Số Điện Thoại", "Địa Chỉ"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
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
        btnSearch.addActionListener(e -> searchCustomer());
        btnReload.addActionListener(e -> reloadData());
    }

    private void searchCustomer() {
        String phone = txtSearch.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại để tìm kiếm!");
            return;
        }
        List<CustomerDTO> list = CustomerBUS.getInstance().findByPhone(phone);
        updateTable(list);
    }

    private void reloadData() {
        txtSearch.setText("");
        model.setRowCount(0);
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

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleControl(JTextField txt) {
        txt.setFont(FONT_TEXT);
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_TEXT);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setMaxWidth(150);
    }
}