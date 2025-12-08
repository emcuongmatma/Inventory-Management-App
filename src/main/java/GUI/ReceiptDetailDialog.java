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

    private final ReceiptDTO receipt;
    private final DecimalFormat df = new DecimalFormat("###,###");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private static final Color COLOR_PRIMARY = new Color(52, 152, 219);
    private static final Color COLOR_BG = Color.WHITE;
    private static final Color COLOR_TEXT = new Color(50, 50, 50);
    private static final Color COLOR_GRAY_BORDER = new Color(230, 230, 230);
    private static final Color COLOR_DANGER = new Color(231, 76, 60);
    private static final Color COLOR_SUCCESS = new Color(46, 204, 113);
    private static final Color COLOR_GRAY_BTN = new Color(149, 165, 166);

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 14);

    public ReceiptDetailDialog(JFrame parent, ReceiptDTO receipt) {
        super(parent, "Chi Tiết Phiếu Nhập", true);
        this.receipt = receipt;
        initUI();
    }

    private void initUI() {
        setSize(950, 650);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_PRIMARY);
        pnlHeader.setBorder(new EmptyBorder(15, 0, 15, 0));

        JLabel lblTitle = new JLabel("CHI TIẾT PHIẾU NHẬP KHO", SwingConstants.CENTER);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);

        return pnlHeader;
    }

    private JPanel createMainContent() {
        JPanel pnlMain = new JPanel(new BorderLayout(0, 20));
        pnlMain.setBackground(COLOR_BG);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

        pnlMain.add(createInfoPanel(), BorderLayout.NORTH);
        pnlMain.add(createTableSection(), BorderLayout.CENTER);

        return pnlMain;
    }

    private JPanel createInfoPanel() {
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 40, 15));
        pnlInfo.setBackground(COLOR_BG);
        pnlInfo.setBorder(new CompoundBorder(
            new LineBorder(COLOR_GRAY_BORDER, 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        pnlInfo.add(createDisplayField("Mã Phiếu Nhập", receipt.get_id().toString()));
        pnlInfo.add(createDisplayField("Thời Gian Tạo", sdf.format(receipt.getReceiptDate())));
        pnlInfo.add(createDisplayField("Nhà Cung Cấp", receipt.getSupplierCode()));
        pnlInfo.add(createDisplayField("Ghi Chú", receipt.getNote() == null ? "Không có" : receipt.getNote()));

        return pnlInfo;
    }

    private JPanel createTableSection() {
        JPanel pnlTableSection = new JPanel(new BorderLayout(0, 10));
        pnlTableSection.setBackground(COLOR_BG);

        String[] headers = {"STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        styleTable(table);

        long totalSum = 0;
        int stt = 1;
        if (receipt.getItems() != null) {
            for (ReceiptItemDTO item : receipt.getItems()) {
                long total = (long) item.getQuantity() * item.getUnitPrice();
                totalSum += total;
                model.addRow(new Object[]{
                    stt++,
                    item.getProductCode(),
                    item.getName(),
                    item.getQuantity(),
                    df.format(item.getUnitPrice()),
                    df.format(total)
                });
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new LineBorder(COLOR_GRAY_BORDER));
        pnlTableSection.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlTotal.setBackground(COLOR_BG);
        
        JLabel lblTotalText = new JLabel("TỔNG TIỀN THANH TOÁN: ");
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalText.setForeground(new Color(100, 100, 100));

        JLabel lblTotalValue = new JLabel(df.format(totalSum) + " VND");
        lblTotalValue.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotalValue.setForeground(COLOR_DANGER);

        pnlTotal.add(lblTotalText);
        pnlTotal.add(lblTotalValue);
        pnlTableSection.add(pnlTotal, BorderLayout.SOUTH);

        return pnlTableSection;
    }

    private JPanel createFooter() {
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlFooter.setBackground(new Color(245, 247, 250));
        pnlFooter.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnPrint = createBtn("In Phiếu", COLOR_SUCCESS);
        JButton btnClose = createBtn("Đóng", COLOR_GRAY_BTN);

        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnPrint);
        pnlFooter.add(btnClose);
        return pnlFooter;
    }

    private JPanel createDisplayField(String title, String value) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_BOLD);
        lblTitle.setForeground(new Color(150, 150, 150));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_PLAIN);
        lblValue.setForeground(COLOR_TEXT);
        lblValue.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, COLOR_GRAY_BORDER),
            new EmptyBorder(0, 0, 5, 0)
        ));

        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        return p;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(FONT_TABLE);
        table.setShowVerticalLines(false);
        table.setGridColor(COLOR_GRAY_BORDER);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        header.setPreferredSize(new Dimension(0, 40));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0, 0, 0, 10));
        table.getColumnModel().getColumn(4).setCellRenderer(right);
        table.getColumnModel().getColumn(5).setCellRenderer(right);
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}