package GUI;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SupplierGUI extends JPanel {

    private JTextField txtCode, txtName, txtEmail, txtAddress, txtPhone, txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private SupplierBUS supplierBUS;
    private JButton btnAdd, btnClear, btnSearch;

    private static final Color COLOR_BG = new Color(245, 247, 250);
    private static final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private static final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private static final Color COLOR_GRAY = new Color(149, 165, 166);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    public SupplierGUI() {
        supplierBUS = new SupplierBUS();
        initUI();
        initEvents();
        loadData();
        resetForm();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlMain = new JPanel(new BorderLayout(0, 20));
        pnlMain.setBackground(COLOR_BG);

        pnlMain.add(createInputPanel(), BorderLayout.NORTH);
        pnlMain.add(createTablePanel(), BorderLayout.CENTER);

        add(pnlMain, BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel pnlInput = new JPanel(new BorderLayout(0, 15));
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80));
        pnlInput.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlFields = new JPanel(new GridLayout(2, 3, 20, 15));
        pnlFields.setBackground(Color.WHITE);

        txtCode = createStyledTextField();
        txtCode.setEditable(false);
        txtCode.setBackground(new Color(240, 240, 240));
        
        txtName = createStyledTextField();
        txtEmail = createStyledTextField();
        txtAddress = createStyledTextField();
        txtPhone = createStyledTextField();

        pnlFields.add(createFieldGroup("Mã NCC:", txtCode));
        pnlFields.add(createFieldGroup("Tên Nhà Cung Cấp:", txtName));
        pnlFields.add(createFieldGroup("Số Điện Thoại:", txtPhone));
        pnlFields.add(createFieldGroup("Email:", txtEmail));
        pnlFields.add(createFieldGroup("Địa Chỉ:", txtAddress));

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlButtons.setBackground(Color.WHITE);

        btnAdd = createButton("Thêm NCC", COLOR_SUCCESS);
        btnClear = createButton("Làm Mới", COLOR_GRAY);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnClear);
        
        pnlFields.add(pnlButtons);

        pnlInput.add(pnlFields, BorderLayout.CENTER);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearch.setBackground(Color.WHITE);
        pnlSearch.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        txtSearch = createStyledTextField();
        txtSearch.setPreferredSize(new Dimension(250, 35));
        btnSearch = createButton("Tìm kiếm", COLOR_PRIMARY);
        
        pnlSearch.add(new JLabel("Tìm kiếm: "));
        pnlSearch.add(txtSearch);
        pnlSearch.add(btnSearch);

        pnlInput.add(pnlSearch, BorderLayout.SOUTH);

        return pnlInput;
    }

    private JPanel createTablePanel() {
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(new LineBorder(new Color(230, 230, 230)));

        String[] columns = {"Mã NCC", "Tên Nhà Cung Cấp", "Email", "Địa Chỉ", "SĐT"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(null);
        
        pnlTable.add(scroll, BorderLayout.CENTER);
        return pnlTable;
    }

    private JPanel createFieldGroup(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_BOLD);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JTextField createStyledTextField() {
        JTextField txt = new JTextField();
        txt.setFont(FONT_PLAIN);
        txt.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_PLAIN);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
    }

    private void initEvents() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtCode.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    txtAddress.setText(tableModel.getValueAt(row, 3).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            SupplierDTO s = new SupplierDTO();
            s.setSupplierCode(txtCode.getText());
            s.setName(txtName.getText());
            s.setEmail(txtEmail.getText());
            s.setAddress(txtAddress.getText());
            s.setPhone(txtPhone.getText());

            String result = supplierBUS.addSupplier(s);
            JOptionPane.showMessageDialog(this, result);

            if (result.contains("thành công")) {
                loadData();
                resetForm();
            }
        });

        btnClear.addActionListener(e -> {
            resetForm();
            loadData();
        });

        ActionListener searchAction = e -> {
            String keyword = txtSearch.getText();
            List<SupplierDTO> list = supplierBUS.searchSupplier(keyword);
            fillTable(list);
        };
        btnSearch.addActionListener(searchAction);
        txtSearch.addActionListener(searchAction);
    }

    public void loadData() {
        List<SupplierDTO> list = supplierBUS.getAllSuppliers();
        fillTable(list);
    }

    private void fillTable(List<SupplierDTO> list) {
        tableModel.setRowCount(0);
        for (SupplierDTO s : list) {
            tableModel.addRow(new Object[]{
                s.getSupplierCode(),
                s.getName(),
                s.getEmail(),
                s.getAddress(),
                s.getPhone()
            });
        }
    }

    private void resetForm() {
        txtCode.setText(supplierBUS.generateNewCode());
        txtName.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtPhone.setText("");
        txtSearch.setText("");
        txtName.requestFocus();
    }
}