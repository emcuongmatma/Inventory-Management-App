/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ReceiptDAO;
import DTO.ReceiptDTO;
import DTO.ReceiptItemDTO;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 *
 * @author ECMM
 */
public class ReceiptBUS {
    private static final ReceiptBUS instance = new ReceiptBUS();
    private ReceiptDAO receiptDAO = new ReceiptDAO();

    public static ReceiptBUS getInstance() {
        return instance;
    }
    
    public boolean addNewReceipt(String supplierCode, String note, List<ReceiptItemDTO> items){
        try {
            ReceiptDTO receipt = new ReceiptDTO(null, supplierCode, note, new Date(), items);
            receiptDAO.insert(receipt); 
            
            for (ReceiptItemDTO item : items) {
                ProductBUS.getInstance().increaseStock(item.getProductCode(), item.getQuantity());
            }
            return true; 
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }    
    
    public List<ReceiptDTO> getAllReceipt(){
        return receiptDAO.getAll();
    }

    public List<ReceiptDTO> getByDate(Date date) {
        Date start = setTime(date, 0, 0, 0);
        Date end = setTime(date, 23, 59, 59);
        return receiptDAO.findByDateRange(start, end);
    }

    public List<ReceiptDTO> filterByDate(Date start, Date end) {
        Date realStart = setTime(start, 0, 0, 0);
        Date realEnd = setTime(end, 23, 59, 59);
        return receiptDAO.findByDateRange(realStart, realEnd);
    }

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