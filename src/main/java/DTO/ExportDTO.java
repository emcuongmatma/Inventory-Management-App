package DTO;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

public class ExportDTO {
    private ObjectId _id;
    private String customerCode; 
    private String note;
    private Date exportDate;
    private Double totalPrice;
    private List<ReceiptItemDTO> items; 

    public ExportDTO() {}

    public ExportDTO(ObjectId _id, String customerCode, String note, Date exportDate, Double totalPrice, List<ReceiptItemDTO> items) {
        this._id = _id;
        this.customerCode = customerCode;
        this.note = note;
        this.exportDate = exportDate;
        this.totalPrice = totalPrice;
        this.items = items;
    }
    public ObjectId get_id() { return _id; }
    public String getCustomerCode() { return customerCode; }
    public String getNote() { return note; }
    public Date getExportDate() { return exportDate; }
    public Double getTotalPrice() { return totalPrice; }
    public List<ReceiptItemDTO> getItems() { return items; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
    public void setNote(String note) { this.note = note; }
    public void setExportDate(Date exportDate) { this.exportDate = exportDate; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public void setItems(List<ReceiptItemDTO> items) { this.items = items; }

    public Object getExportCode() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}