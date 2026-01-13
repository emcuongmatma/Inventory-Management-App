package GUI;

import DTO.ReceiptDTO;
import DTO.ReceiptItemDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
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
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
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

        JButton btnPrint = createBtn("In Phiếu (PDF)", COLOR_SUCCESS);
        JButton btnClose = createBtn("Đóng", COLOR_GRAY_BTN);

        btnPrint.addActionListener(e -> printToPDF());
        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnPrint);
        pnlFooter.add(btnClose);
        return pnlFooter;
    }

    private void printToPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu phiếu nhập");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
            fileChooser.setSelectedFile(new File("PhieuNhap_" + receipt.get_id() + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

               
                String fontPath = "C:\\Windows\\Fonts\\arial.ttf";
                BaseFont bf;
                try {
                    bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                } catch (Exception e) {
                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                }

                com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(bf, 20, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontBold = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(bf, 12, com.itextpdf.text.Font.NORMAL);

             
                Paragraph title = new Paragraph("CHI TIẾT PHIẾU NHẬP", fontTitle);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                document.add(new Paragraph("Mã phiếu: " + receipt.get_id(), fontBold));
                document.add(new Paragraph("Ngày nhập: " + sdf.format(receipt.getReceiptDate()), fontNormal));
                document.add(new Paragraph("Nhà cung cấp: " + receipt.getSupplierCode(), fontNormal));
                document.add(new Paragraph("Ghi chú: " + (receipt.getNote() == null ? "" : receipt.getNote()), fontNormal));
                document.add(new Paragraph(" ", fontNormal));

           
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);
                table.setWidths(new float[]{1f, 2f, 4f, 1.5f, 2.5f, 3f});

                addTableHeader(table, fontBold, "STT", "Mã SP", "Tên Sản Phẩm", "SL", "Đơn Giá", "Thành Tiền");

                long totalSum = 0;
                int stt = 1;
                if (receipt.getItems() != null) {
                    for (ReceiptItemDTO item : receipt.getItems()) {
                        long total = (long) item.getQuantity() * item.getUnitPrice();
                        totalSum += total;

                        addTableCell(table, fontNormal, String.valueOf(stt++), Element.ALIGN_CENTER);
                        addTableCell(table, fontNormal, item.getProductCode(), Element.ALIGN_LEFT);
                        addTableCell(table, fontNormal, item.getName(), Element.ALIGN_LEFT);
                        addTableCell(table, fontNormal, String.valueOf(item.getQuantity()), Element.ALIGN_CENTER);
                        addTableCell(table, fontNormal, df.format(item.getUnitPrice()), Element.ALIGN_RIGHT);
                        addTableCell(table, fontNormal, df.format(total), Element.ALIGN_RIGHT);
                    }
                }
                document.add(table);

                Paragraph pTotal = new Paragraph("Tổng cộng: " + df.format(totalSum) + " VND", fontBold);
                pTotal.setAlignment(Element.ALIGN_RIGHT);
                pTotal.setSpacingBefore(15);
                document.add(pTotal);

                Paragraph pSign = new Paragraph("\nNgười lập phiếu", fontNormal);
                pSign.setAlignment(Element.ALIGN_RIGHT);
                pSign.setIndentationRight(50);
                document.add(pSign);

                document.close();

                int choice = JOptionPane.showConfirmDialog(this, "Xuất PDF thành công! Mở file ngay?", "Thành công", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(filePath));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTableHeader(PdfPTable table, com.itextpdf.text.Font font, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }

    private void addTableCell(PdfPTable table, com.itextpdf.text.Font font, String value, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(value, font));
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        table.addCell(cell);
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
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));
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
        btn.setPreferredSize(new Dimension(140, 38));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}