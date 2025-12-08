package DTO;

import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;

public class ReceiptDTO {
    private ObjectId _id; 
    private String supplierCode;
    private String note;
    private Date receiptDate;
    private List<ReceiptItemDTO> items;

    public ReceiptDTO() {
    }

    public ReceiptDTO(ObjectId _id, String supplierCode, String note, Date receiptDate, List<ReceiptItemDTO> items) {
        this._id = _id;
        this.supplierCode = supplierCode;
        this.note = note;
        this.receiptDate = receiptDate;
        this.items = items;
    }

    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }

    public String getSupplierCode() { return supplierCode; }
    public void setSupplierCode(String supplierCode) { this.supplierCode = supplierCode; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Date getReceiptDate() { return receiptDate; }
    public void setReceiptDate(Date receiptDate) { this.receiptDate = receiptDate; }

    public List<ReceiptItemDTO> getItems() { return items; }
    public void setItems(List<ReceiptItemDTO> items) { this.items = items; }
}