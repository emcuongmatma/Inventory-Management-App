package GUI;

import BUS.ReceiptBUS;
import DTO.ReceiptDTO;
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
import java.util.Date;
import java.util.List;

public class ReceiptGUI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JSpinner txtDate; // Ô chọn ngày tìm kiếm
    private DecimalFormat df = new DecimalFormat("###,### VND");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private List<ReceiptDTO> receiptList;

    // --- MÀU SẮC GIAO DIỆN (FLAT UI) ---
    private final Color COLOR_BG = new Color(245, 247, 250);
    private final Color COLOR_PRIMARY = new Color(52, 152, 219); // Xanh dương
    private final Color COLOR_SUCCESS = new Color(46, 204, 113); // Xanh lá
    private final Color COLOR_WARNING = new Color(230, 126, 34); // Cam (Nút tìm)
    private final Color COLOR_GRAY = new Color(149, 165, 166);   // Xám
    private final Color COLOR_TEXT = new Color(50, 50, 50);

    public ReceiptGUI() {
        setLayout(new BorderLayout());
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding bao quanh

        // =========================================================================
        // === 1. HEADER & TOOLBAR ===
        // =========================================================================
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Title
        JLabel lblTitle = new JLabel("QUẢN LÝ NHẬP KHO");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        pnlHeader.add(lblTitle, BorderLayout.NORTH);

        // Toolbar Container
        JPanel pnlTool = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlTool.setBackground(Color.WHITE);
        // pnlTool.setBorder(new MatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        // --- NHÓM NÚT CHỨC NĂNG ---
        JButton btnAdd = createButton("Tạo Phiếu Nhập", COLOR_SUCCESS);
        JButton btnDetail = createButton("Xem Chi Tiết", COLOR_PRIMARY);
        
        // --- NHÓM TÌM KIẾM NGÀY ---
        JLabel lblDate = new JLabel("Ngày nhập:");
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDate.setForeground(COLOR_TEXT);

        // Cấu hình JSpinner để chọn ngày
        txtDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(txtDate, "dd/MM/yyyy");
        txtDate.setEditor(editor);
        txtDate.setPreferredSize(new Dimension(130, 38));
        txtDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDate.setValue(new Date()); // Mặc định hôm nay

        JButton btnSearch = createButton("Tìm", COLOR_WARNING);
        btnSearch.setPreferredSize(new Dimension(80, 38));

        JButton btnRefresh = createButton("Làm Mới", COLOR_GRAY);

        // Add components to Toolbar
        pnlTool.add(btnAdd);
        pnlTool.add(btnDetail);
        pnlTool.add(Box.createHorizontalStrut(30)); // Khoảng cách giữa 2 nhóm
        pnlTool.add(lblDate);
        pnlTool.add(txtDate);
        pnlTool.add(btnSearch);
        pnlTool.add(btnRefresh);

        pnlHeader.add(pnlTool, BorderLayout.CENTER);

        // =========================================================================
        // === 2. TABLE ===
        // =========================================================================
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBackground(Color.WHITE);
        pnlTable.setBorder(new EmptyBorder(0, 20, 20, 20));

        String[] headers = {"STT", "Mã Phiếu", "Nhà Cung Cấp", "Thời Gian Tạo", "Ghi Chú", "Tổng Tiền"};
        model = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table); // Áp dụng style đẹp

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new LineBorder(new Color(230, 230, 230)));
        
        pnlTable.add(scroll, BorderLayout.CENTER);

        // Wrap Header & Table vào Card Layout giả lập
        JPanel pnlCard = new JPanel(new BorderLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.add(pnlHeader, BorderLayout.NORTH);
        pnlCard.add(pnlTable, BorderLayout.CENTER);

        add(pnlCard, BorderLayout.CENTER);

        // =========================================================================
        // === 3. EVENTS ===
        // =========================================================================

        // Sự kiện Thêm mới
        btnAdd.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            ReceiptCreateDialog dlg = new ReceiptCreateDialog(parent);
            dlg.setVisible(true);
            if(dlg.isSuccess()) loadData(); // Load lại nếu thêm thành công
        });

        // Sự kiện Xem chi tiết
        btnDetail.addActionListener(e -> showDetail());

        // Sự kiện Click đúp bảng
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) showDetail();
            }
        });

        // Sự kiện Tìm kiếm theo ngày
        btnSearch.addActionListener(e -> {
            try {
                Date date = (Date) txtDate.getValue();
                // Gọi BUS tìm theo ngày (Hàm getByDate đã tạo ở bước trước)
                receiptList = ReceiptBUS.getInstance().getByDate(date);
                renderTable(); // Chỉ render lại list kết quả
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày!");
            }
        });

        // Sự kiện Làm mới (Reset về tất cả)
        btnRefresh.addActionListener(e -> {
            txtDate.setValue(new Date()); // Reset ô ngày về hôm nay
            loadData(); // Load tất cả từ DB
        });

        // Load dữ liệu ban đầu
        loadData();
    }

    // =========================================================================
    // === HELPER METHODS ===
    // =========================================================================

    private void showDetail() {
        int row = table.getSelectedRow();
        if(row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần xem!");
            return;
        }
        ReceiptDTO selectedReceipt = receiptList.get(row);
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ReceiptDetailDialog dlg = new ReceiptDetailDialog(parent, selectedReceipt);
        dlg.setVisible(true);
    }

    // Hàm load tất cả dữ liệu từ DB
    public void loadData() {
        receiptList = ReceiptBUS.getInstance().getAllReceipt();
        renderTable();
    }

    // Hàm hiển thị dữ liệu ra bảng
    private void renderTable() {
        model.setRowCount(0);
        int stt = 1;
        if(receiptList != null) {
            for (ReceiptDTO r : receiptList) {
                // Tính tổng tiền an toàn (tránh null)
                long total = 0;
                if(r.getItems() != null) {
                    for(ReceiptItemDTO item : r.getItems()) {
                        long qty = item.getQuantity() == null ? 0 : item.getQuantity();
                        long price = item.getUnitPrice() == null ? 0 : item.getUnitPrice();
                        total += qty * price;
                    }
                }
                
                // Lấy ID (tránh null)
                String idStr = (r.get_id() != null) ? r.get_id().toString() : "";
                
                model.addRow(new Object[]{
                    stt++,
                    idStr,
                    r.getSupplierCode(),
                    sdf.format(r.getReceiptDate()),
                    r.getNote(),
                    df.format(total)
                });
            }
        }
    }

    // Style nút bấm
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        
        return btn;
    }

    // Style bảng dữ liệu
    private void styleTable(JTable table) {
        table.setRowHeight(40); // Tăng chiều cao dòng cho thoáng
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(0, 40));

        // Alignment
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0, 0, 0, 10)); // Padding phải cho số tiền

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(JLabel.LEFT);
        left.setBorder(new EmptyBorder(0, 10, 0, 0)); // Padding trái cho text

        table.getColumnModel().getColumn(0).setCellRenderer(center); // STT
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        
        table.getColumnModel().getColumn(1).setCellRenderer(center); // Mã
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        
        table.getColumnModel().getColumn(2).setCellRenderer(left);   // NCC
        table.getColumnModel().getColumn(3).setCellRenderer(center); // Ngày
        table.getColumnModel().getColumn(4).setCellRenderer(left);   // Note
        table.getColumnModel().getColumn(5).setCellRenderer(right);  // Tổng tiền
    }
}