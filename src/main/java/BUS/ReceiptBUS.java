/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ReceiptDAO;
import com.mycompany.inventorymanagementapp.DTO.ReceiptDTO;
import com.mycompany.inventorymanagementapp.DTO.ReceiptItemDTO;
import java.util.Date;
import java.util.List;

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
    
    public void addNewReceipt(String supplierCode,String note, List<ReceiptItemDTO> items){
        ReceiptDTO receipt = new ReceiptDTO(null, supplierCode, note, new Date(), items);
        receiptDAO.insert(receipt);
    }   
    
    public List<ReceiptDTO> getAllReceipt(){
        return receiptDAO.getAll();
    }
}
