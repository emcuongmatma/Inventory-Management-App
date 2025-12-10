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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class CustomerGUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnSearch, btnReload;

    private static final Color COLOR_BG = new Color(240, 242, 245);
    private static final Color COLOR_PRIMARY = new Color(66, 133, 244);
    private static final Color COLOR_GRAY = new Color(149, 165, 166);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public CustomerGUI() {
        initUI();
        initEvents();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 20));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 30, 30, 30));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createTableSection(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel pnlTop = new JPanel(new BorderLayout(20, 10));
        pnlTop.setBackground(COLOR_BG);

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(new Color(44, 62, 80));

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlSearch.setBackground(COLOR_BG);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(FONT_BOLD);
        
        txtSearch = new JTextField(20);
        styleControl(txtSearch);
        txtSearch.putClientProperty("JTextField.placeholderText", "Nhập SĐT khách hàng...");

        btnSearch = createBtn("Tìm", COLOR_PRIMARY);
        btnReload = createBtn("Làm Mới", COLOR_GRAY);

        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);
        pnlSearch.add(btnReload);

        pnlTop.add(lblTitle, BorderLayout.WEST);
        pnlTop.add(pnlSearch, BorderLayout.EAST);

        return pnlTop;
    }

    private JScrollPane createTableSection() {
        String[] headers = {"STT", "Họ và Tên", "Số Điện Thoại", "Địa Chỉ"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        
        return scrollPane;
    }

    private void initEvents() {
        btnSearch.addActionListener(e -> searchCustomer());
        btnReload.addActionListener(e -> reloadData());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                reloadData();
            }
        });
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
        List<CustomerDTO> list = CustomerBUS.getInstance().getAllCustomers();
        updateTable(list);
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
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleControl(JTextField txt) {
        txt.setFont(FONT_TEXT);
        txt.setPreferredSize(new Dimension(250, 38));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(FONT_TEXT);
        table.setShowVerticalLines(true); 
        table.setShowHorizontalLines(true); 
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(new Color(44, 62, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setMaxWidth(180);
    }
}