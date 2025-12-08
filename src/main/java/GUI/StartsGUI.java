package GUI;

import BUS.ExportBUS;
import BUS.ReceiptBUS;
import DTO.ExportDTO;
import DTO.ReceiptDTO;
import DTO.ReceiptItemDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StartsGUI extends JPanel {
    // Components
    private JLabel lblTotalImport, lblTotalExport, lblProfit;
    private JSpinner txtDateFrom, txtDateTo;
    private JTabbedPane tabbedPane;
    private DefaultTableModel modelImport, modelExport;
    private SimpleChartPanel chartPanel; // Panel vẽ biểu đồ

    // Formatters
    private DecimalFormat df = new DecimalFormat("###,### VND");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    // Colors
    private final Color COLOR_IMPORT = new Color(231, 76, 60);  // Đỏ
    private final Color COLOR_EXPORT = new Color(46, 204, 113); // Xanh lá
    private final Color COLOR_PROFIT = new Color(52, 152, 219); // Xanh dương

    public StartsGUI() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 1. FILTER BAR (TOP) ---
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlFilter.setBackground(Color.WHITE);
        pnlFilter.setBorder(new LineBorder(new Color(230, 230, 230), 1, true));

        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(44, 62, 80));

        // Date Spinners
        txtDateFrom = createDateSpinner();
        txtDateTo = createDateSpinner();
        
        // Mặc định: Từ đầu tháng đến hiện tại
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        txtDateFrom.setValue(cal.getTime());

        JButton btnFilter = new JButton("Thống Kê");
        btnFilter.setBackground(new Color(241, 196, 15));
        btnFilter.setForeground(Color.WHITE);
        btnFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFilter.setPreferredSize(new Dimension(100, 30));
        
        JButton btnRefresh = new JButton("Toàn bộ thời gian");
        btnRefresh.setBackground(new Color(149, 165, 166));
        btnRefresh.setForeground(Color.WHITE);

        pnlFilter.add(lblTitle);
        pnlFilter.add(Box.createHorizontalStrut(20));
        pnlFilter.add(new JLabel("Từ:"));
        pnlFilter.add(txtDateFrom);
        pnlFilter.add(new JLabel("Đến:"));
        pnlFilter.add(txtDateTo);
        pnlFilter.add(btnFilter);
        pnlFilter.add(btnRefresh);

        add(pnlFilter, BorderLayout.NORTH);

        // --- 2. CENTER PANEL (CARDS + CHART + TABLE) ---
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 20));
        pnlCenter.setOpaque(false);

        // 2a. Summary Cards
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setOpaque(false);
        pnlCards.setPreferredSize(new Dimension(0, 120));

        JPanel cardImport = createCard("TỔNG CHI (NHẬP)", COLOR_IMPORT);
        JPanel cardExport = createCard("TỔNG THU (XUẤT)", COLOR_EXPORT);
        JPanel cardProfit = createCard("LỢI NHUẬN", COLOR_PROFIT);

        lblTotalImport = (JLabel) cardImport.getComponent(1);
        lblTotalExport = (JLabel) cardExport.getComponent(1);
        lblProfit = (JLabel) cardProfit.getComponent(1);

        pnlCards.add(cardImport);
        pnlCards.add(cardExport);
        pnlCards.add(cardProfit);
        
        pnlCenter.add(pnlCards, BorderLayout.NORTH);

        // 2b. Details (Tabs & Chart)
        JPanel pnlDetails = new JPanel(new BorderLayout(20, 0));
        pnlDetails.setOpaque(false);

        // Chart (Left)
        chartPanel = new SimpleChartPanel();
        chartPanel.setPreferredSize(new Dimension(300, 0));
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new LineBorder(new Color(230, 230, 230)));
        
        // Tables (Right)
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Table Import
        modelImport = new DefaultTableModel(new String[]{"Ngày", "Mã Phiếu", "NCC", "Chi Tiền"}, 0);
        JTable tblImport = new JTable(modelImport);
        styleTable(tblImport);
        tabbedPane.addTab("Lịch sử Nhập Hàng", new JScrollPane(tblImport));

        // Table Export
        modelExport = new DefaultTableModel(new String[]{"Ngày", "Mã HĐ", "Khách Hàng", "Thu Tiền"}, 0);
        JTable tblExport = new JTable(modelExport);
        styleTable(tblExport);
        tabbedPane.addTab("Lịch sử Xuất Hàng", new JScrollPane(tblExport));

        pnlDetails.add(chartPanel, BorderLayout.WEST);
        pnlDetails.add(tabbedPane, BorderLayout.CENTER);

        pnlCenter.add(pnlDetails, BorderLayout.CENTER);

        add(pnlCenter, BorderLayout.CENTER);

       
        btnFilter.addActionListener(e -> filterData());
        btnRefresh.addActionListener(e -> {
            // Reset date và load all
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1); // Set về quá khứ xa
            txtDateFrom.setValue(c.getTime());
            txtDateTo.setValue(new Date());
            filterData();
        });

       
        filterData();
    }

    // Logic thống kê chính
    private void filterData() {
        try {
            Date start = (Date) txtDateFrom.getValue();
            Date end = (Date) txtDateTo.getValue();


            List<ReceiptDTO> receipts = ReceiptBUS.getInstance().filterByDate(start, end);
            List<ExportDTO> exports = ExportBUS.getInstance().filterByDate(start, end);

            // 2. Tính toán & Đổ vào bảng Nhập
            double totalImp = 0;
            modelImport.setRowCount(0);
            if (receipts != null) {
                for (ReceiptDTO r : receipts) {
                    double billTotal = 0;
                    if(r.getItems() != null) {
                        for(ReceiptItemDTO item : r.getItems()) {
                             long q = item.getQuantity() == null ? 0 : item.getQuantity();
                             long p = item.getUnitPrice() == null ? 0 : item.getUnitPrice(); // Hoặc getPrice() tùy DTO bạn sửa chưa
                             billTotal += q * p;
                        }
                    }
                    totalImp += billTotal;
                    modelImport.addRow(new Object[]{
                        sdf.format(r.getReceiptDate()),
                        r.get_id(), // ObjectId
                        r.getSupplierCode(),
                        df.format(billTotal)
                    });
                }
            }

            // 3. Tính toán & Đổ vào bảng Xuất
            double totalExp = 0;
            modelExport.setRowCount(0);
            if (exports != null) {
                for (ExportDTO ex : exports) {
                    double billTotal = ex.getTotalPrice() == null ? 0 : ex.getTotalPrice();
                    totalExp += billTotal;
                    modelExport.addRow(new Object[]{
                        sdf.format(ex.getExportDate()),
                        ex.get_id(),
                        ex.getCustomerCode(),
                        df.format(billTotal)
                    });
                }
            }

            // 4. Update UI Card
            lblTotalImport.setText(df.format(totalImp));
            lblTotalExport.setText(df.format(totalExp));
            lblProfit.setText(df.format(totalExp - totalImp));

            // 5. Update Chart
            chartPanel.updateValues(totalImp, totalExp);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi thống kê: " + ex.getMessage());
        }
    }

    // --- INNER CLASS: BIỂU ĐỒ ĐƠN GIẢN (Tự vẽ) ---
    class SimpleChartPanel extends JPanel {
        private double impVal = 0;
        private double expVal = 0;

        public void updateValues(double imp, double exp) {
            this.impVal = imp;
            this.expVal = exp;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int pad = 40;
            int barWidth = 60;
            
            // Title Chart
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            g2.drawString("BIỂU ĐỒ THU CHI", w/2 - 60, 25);

            // Tính tỷ lệ chiều cao
            double max = Math.max(impVal, expVal);
            if (max == 0) max = 1; // Tránh chia cho 0
            
            int hChart = h - pad * 2;
            int hImp = (int) ((impVal / max) * hChart);
            int hExp = (int) ((expVal / max) * hChart);

            // Vẽ cột Chi (Nhập) - Đỏ
            int xImp = w/2 - barWidth - 10;
            int yImp = h - pad - hImp;
            g2.setColor(COLOR_IMPORT);
            g2.fillRoundRect(xImp, yImp, barWidth, hImp, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString("Chi", xImp + 20, h - pad + 15);

            // Vẽ cột Thu (Xuất) - Xanh
            int xExp = w/2 + 10;
            int yExp = h - pad - hExp;
            g2.setColor(COLOR_EXPORT);
            g2.fillRoundRect(xExp, yExp, barWidth, hExp, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString("Thu", xExp + 20, h - pad + 15);
            
            // Vẽ trục
            g2.setColor(Color.GRAY);
            g2.drawLine(pad, h - pad, w - pad, h - pad); // Trục X
        }
    }

    // --- HELPERS ---
    private JSpinner createDateSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(editor);
        spinner.setPreferredSize(new Dimension(120, 30));
        return spinner;
    }
public void loadData() {
        filterData(); // Gọi lại logic lọc dữ liệu hiện tại
    }
    private JPanel createCard(String title, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(color);
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setForeground(new Color(255, 255, 255, 220));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel lblValue = new JLabel("0 VND", SwingConstants.CENTER);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Font nhỏ hơn xíu cho vừa
        
        p.add(lblTitle, BorderLayout.NORTH);
        p.add(lblValue, BorderLayout.CENTER);
        return p;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setShowVerticalLines(false);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center); // Ngày
        
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0,0,0,10));
        table.getColumnModel().getColumn(3).setCellRenderer(right); // Tiền
    }
}