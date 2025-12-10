package GUI;

import BUS.ReceiptBUS;
import BUS.SupplierBUS;
import DTO.ReceiptDTO;
import DTO.ReceiptItemDTO;
import DTO.SupplierDTO;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGUI extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JSpinner txtDate;
    private JButton btnAdd, btnDetail, btnSearch, btnRefresh;

    private List<ReceiptDTO> receiptList;
    
    private final SupplierBUS supplierBUS; 
    private final Map<String, String> supplierMap; 

    private final DecimalFormat df = new DecimalFormat("###,### VND");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final Color COLOR_BG = new Color(245, 247, 250);
    private static final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private static final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private static final Color COLOR_WARNING = new Color(230, 126, 34);
    private static final Color COLOR_GRAY = new Color(149, 165, 166);
    private static final Color COLOR_TEXT = new Color(50, 50, 50);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    public ReceiptGUI() {

        supplierBUS = new SupplierBUS();
        supplierMap = new HashMap<>();

        initUI();
        initEvents();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlCard = new JPanel(new BorderLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.add(createHeader(), BorderLayout.NORTH);
        pnlCard.add(createTableSection(), BorderLayout.CENTER);

        add(pnlCard, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHẬP KHO");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(new Color(44, 62, 80));
        pnlHeader.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlTool.setBackground(Color.WHITE);

        btnAdd = createButton("Tạo Phiếu Nhập", COLOR_SUCCESS);
        btnDetail = createButton("Xem Chi Tiết", COLOR_PRIMARY);

        JLabel lblDate = new JLabel("Ngày nhập:");
        lblDate.setFont(FONT_BOLD);
        lblDate.setForeground(COLOR_TEXT);

        txtDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(txtDate, "dd/MM/yyyy");
        txtDate.setEditor(editor);
        txtDate.setPreferredSize(new Dimension(130, 38));
        txtDate.setFont(FONT_PLAIN);
        txtDate.setValue(new Date());

        btnSearch = createButton("Tìm", COLOR_WARNING);
        btnSearch.setPreferredSize(new Dimension(80, 38));
        btnRefresh = createButton("Làm Mới", COLOR_GRAY);

        pnlTool.add(btnAdd);
        pnlTool.add(btnDetail);
        pnlTool.add(Box.createHorizontalStrut(30));
        pnlTool.add(lblDate);
        pnlTool.add(txtDate);
        pnlTool.add(btnSearch);
        pnlTool.add(btnRefresh);

        pnlHeader.add(pnlTool, BorderLayout.CENTER);
        return pnlHeader;
    }

    private JPanel createTableSection() {
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(new EmptyBorder(0, 20, 20, 20));

        String[] headers = {"STT", "Mã Phiếu", "Nhà Cung Cấp", "Thời Gian Tạo", "Ghi Chú", "Tổng Tiền"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new LineBorder(new Color(230, 230, 230)));

        pnlTable.add(scroll, BorderLayout.CENTER);
        return pnlTable;
    }

    private void initEvents() {
        btnAdd.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            ReceiptCreateDialog dlg = new ReceiptCreateDialog(parent);
            dlg.setVisible(true);
            if (dlg.isSuccess()) loadData();
        });

        btnDetail.addActionListener(e -> showDetail());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) showDetail();
            }
        });

        btnSearch.addActionListener(e -> {
            try {
                Date date = (Date) txtDate.getValue();
                receiptList = ReceiptBUS.getInstance().getByDate(date);
                renderTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày!");
            }
        });

        btnRefresh.addActionListener(e -> {
            txtDate.setValue(new Date());
            loadData();
        });
    }

    private void showDetail() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xem!");
            return;
        }
        ReceiptDTO selectedReceipt = receiptList.get(row);
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ReceiptDetailDialog dlg = new ReceiptDetailDialog(parent, selectedReceipt);
        dlg.setVisible(true);
    }



    public void loadData() {
        loadSupplierCache();

        receiptList = ReceiptBUS.getInstance().getAllReceipt();

        renderTable();
    }

    private void loadSupplierCache() {
        supplierMap.clear();
        List<SupplierDTO> list = supplierBUS.getAllSuppliers();
        if (list != null) {
            for (SupplierDTO s : list) {
                supplierMap.put(s.getSupplierCode(), s.getName());
            }
        }
    }

    private void renderTable() {
        model.setRowCount(0);
        int stt = 1;
        if (receiptList != null) {
            for (ReceiptDTO r : receiptList) {
                long total = 0;
                if (r.getItems() != null) {
                    for (ReceiptItemDTO item : r.getItems()) {
                        long qty = item.getQuantity() == null ? 0 : item.getQuantity();
                        long price = item.getUnitPrice() == null ? 0 : item.getUnitPrice();
                        total += qty * price;
                    }
                }

                String idStr = (r.get_id() != null) ? r.get_id().toString() : "";
                
                String supplierName = supplierMap.getOrDefault(r.getSupplierCode(), r.getSupplierCode());

                model.addRow(new Object[]{
                    stt++,
                    idStr,
                    supplierName,
                    sdf.format(r.getReceiptDate()),
                    r.getNote(),
                    df.format(total)
                });
            }
        }
    }


    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });

        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(FONT_PLAIN);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0, 0, 0, 10));

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(JLabel.LEFT);
        left.setBorder(new EmptyBorder(0, 10, 0, 0));

        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setCellRenderer(left); // Cột Tên NCC canh trái
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(left);
        table.getColumnModel().getColumn(5).setCellRenderer(right); // Cột Tiền canh phải
    }
}