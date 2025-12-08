
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import DTO.ReceiptDTO;
import DTO.ReceiptItemDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import java.util.Date;
import org.bson.conversions.Bson;

public class ReceiptDAO {

    private final MongoCollection<Document> collection;

    public ReceiptDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("receipts");
    }

    public void insert(ReceiptDTO receipt) {
        Document doc = new Document()
                .append("supplierCode", receipt.getSupplierCode())
                .append("note", receipt.getNote())
                .append("receiptDate", receipt.getReceiptDate())
                .append("items", receipt.getItems());
        collection.insertOne(doc);
    }

    public List<ReceiptDTO> getAll() {
        List<ReceiptDTO> list = new ArrayList<>();
        for (Document doc : collection.find().sort(Sorts.descending("receiptDate"))) {
            list.add(convert(doc));
        }
        return list;
    }
public List<ReceiptDTO> findByDateRange(Date start, Date end) {
    List<ReceiptDTO> list = new ArrayList<>();
    Bson filter = Filters.and(
        Filters.gte("receiptDate", start),
        Filters.lte("receiptDate", end)
    );

    for (Document doc : collection.find(filter).sort(Sorts.descending("receiptDate"))) {
        list.add(convert(doc));
    }
    return list;
}
private ReceiptDTO convert(Document doc) {
    ReceiptDTO p = new ReceiptDTO();
    p.set_id(doc.getObjectId("_id")); 
    
    p.setNote(doc.getString("note"));
    p.setSupplierCode(doc.getString("supplierCode"));
    p.setReceiptDate(doc.getDate("receiptDate"));
    
    List<Document> itemDocs = (List<Document>) doc.get("items");
    if (itemDocs != null) {
        List<ReceiptItemDTO> items = itemDocs.stream()
                .map(d -> new ReceiptItemDTO(
                        d.getString("productCode"),
                        d.getString("name"),
                        d.getInteger("quantity", 0),
                        
                     
                        d.getInteger("price") != null ? d.getInteger("price") : d.getInteger("unitPrice", 0)
                ))
                .toList();
        p.setItems(items);
    }
    return p;
}}