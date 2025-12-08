package BUS;

import DAO.ExportDAO;
import DTO.ExportDTO;
import DTO.ReceiptItemDTO;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
public class ExportBUS {
    private static final ExportBUS instance = new ExportBUS();
    private ExportDAO exportDAO = new ExportDAO();

    public static ExportBUS getInstance() {
        return instance;
    }

    public boolean addNewExport(String customerCode, String note, List<ReceiptItemDTO> items) {
        // 1. Tính tổng tiền
        double total = 0;
        for (ReceiptItemDTO item : items) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        // 2. Trừ tồn kho (Quan trọng)
        for (ReceiptItemDTO item : items) {
            boolean result = ProductBUS.getInstance().decreaseStock(item.getProductCode(), item.getQuantity());
            if (!result) return false; // Nếu không đủ hàng thì hủy
        }

        // 3. Lưu hóa đơn
        ExportDTO export = new ExportDTO(null, customerCode, note, new Date(), total, items);
        exportDAO.insert(export);
        return true;
    }

    public List<ExportDTO> getAllExports() {
        return exportDAO.getAll();
    }

    public boolean addNewExport(String text, String admin, String xuất_bán, List<ReceiptItemDTO> cart) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public List<ExportDTO> filterByDate(Date start, Date end) {
    Date realStart = setTime(start, 0, 0, 0);
    Date realEnd = setTime(end, 23, 59, 59);
    return exportDAO.findByDateRange(realStart, realEnd);
}

// Hàm phụ trợ set thời gian (Copy từ ReceiptBUS sang nếu chưa có)
private Date setTime(Date date, int h, int m, int s) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, h);
    cal.set(Calendar.MINUTE, m);
    cal.set(Calendar.SECOND, s);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
}
}