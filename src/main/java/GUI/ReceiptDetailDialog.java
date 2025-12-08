package GUI;

import DTO.ReceiptDTO;
import DTO.ReceiptItemDTO;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class ReceiptDetailDialog extends JDialog {

    private ReceiptDTO receipt;
    private DecimalFormat df = new DecimalFormat("###,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Màu sắc chủ đạo
    private final Color COLOR_PRIMARY = new Color(52, 152, 219); // Xanh dương hiện đại
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXT = new Color(50, 50, 50);

    public ReceiptDetailDialog(JFrame parent, ReceiptDTO receipt) {
        super(parent, "Chi Tiết Phiếu Nhập", true);
        this.receipt = receipt;
        setSize(950, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        // --- 1. HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_PRIMARY);
        pnlHeader.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU NHẬP KHO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT (INFO + TABLE) ---
        JPanel pnlMain = new JPanel(new BorderLayout(0, 20));
        pnlMain.setBackground(COLOR_BG);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

        // A. Thông tin chung (Info Panel)
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 40, 15)); // 2 hàng, 2 cột lớn, khoảng cách rộng
        pnlInfo.setBackground(COLOR_BG);
        pnlInfo.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Dữ liệu mẫu cho Nhân viên (vì DTO chưa có)
        String staffName = "Admin"; 
        
        pnlInfo.add(createDisplayField("Mã Phiếu Nhập", receipt.get_id().toString()));
        pnlInfo.add(createDisplayField("Thời Gian Tạo", sdf.format(receipt.getReceiptDate())));
        pnlInfo.add(createDisplayField("Nhà Cung Cấp", receipt.getSupplierCode()));
        pnlInfo.add(createDisplayField("Ghi Chú", receipt.getNote() == null ? "Không có" : receipt.getNote()));

        pnlMain.add(pnlInfo, BorderLayout.NORTH);

        // B. Bảng chi tiết (Table)
        String[] headers = {"STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        // Đổ dữ liệu
        int stt = 1;
        long totalSum = 0;
        if (receipt.getItems() != null) {
            for (ReceiptItemDTO item : receipt.getItems()) {
                long total = (long) item.getQuantity() * item.getUnitPrice(); // Lưu ý: DTO dùng getPrice() hay getUnitPrice() thì sửa lại cho khớp
                totalSum += total;
                model.addRow(new Object[]{
                    stt++,
                    item.getProductCode(),
                    item.getName(), // Hoặc getProductName() tùy DTO
                    item.getQuantity(),
                    df.format(item.getUnitPrice()),
                    df.format(total)
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        pnlMain.add(scrollPane, BorderLayout.CENTER);

        // C. Tổng tiền (Total)
        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTotal.setBackground(COLOR_BG);
        
        JLabel lblTotalText = new JLabel("TỔNG TIỀN THANH TOÁN: ");
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalText.setForeground(new Color(100, 100, 100));

        JLabel lblTotalValue = new JLabel(df.format(totalSum) + " VND");
        lblTotalValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalValue.setForeground(new Color(231, 76, 60)); // Màu đỏ nổi bật

        pnlTotal.add(lblTotalText);
        pnlTotal.add(lblTotalValue);
        pnlMain.add(pnlTotal, BorderLayout.SOUTH);

        add(pnlMain, BorderLayout.CENTER);

        // --- 3. FOOTER ACTIONS ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlFooter.setBackground(new Color(245, 247, 250));
        pnlFooter.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnPrint = createBtn("In Phiếu", new Color(46, 204, 113)); // Xanh lá
        JButton btnClose = createBtn("Đóng", new Color(149, 165, 166)); // Xám

        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnPrint);
        pnlFooter.add(btnClose);
        add(pnlFooter, BorderLayout.SOUTH);
    }

    // =========================================================================
    // === HELPER METHODS ===
    // =========================================================================

    // Tạo nhóm Label + Text hiển thị thông tin đẹp
    private JPanel createDisplayField(String title, String value) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(new Color(150, 150, 150));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblValue.setForeground(COLOR_TEXT);
        // Thêm border dưới chân để tạo cảm giác dòng kẻ
        lblValue.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            new EmptyBorder(0, 0, 5, 0)
        ));

        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        return p;
    }

    // Style cho bảng
    private void styleTable(JTable table) {
        table.setRowHeight(40); // Hàng cao thoáng
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setShowVerticalLines(false); // Bỏ kẻ dọc
        table.setGridColor(new Color(230, 230, 230));
        table.setFillsViewportHeight(true);

        // Header Style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(0, 40));

        // Căn giữa STT, Mã, SL
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center); // STT
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setCellRenderer(center); // Mã SP
        table.getColumnModel().getColumn(3).setCellRenderer(center); // SL

        // Căn phải Tiền
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0, 0, 0, 10)); // Padding phải
        table.getColumnModel().getColumn(4).setCellRenderer(right); // Đơn giá
        table.getColumnModel().getColumn(5).setCellRenderer(right); // Thành tiền
    }

    // Tạo nút bấm phẳng
    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}