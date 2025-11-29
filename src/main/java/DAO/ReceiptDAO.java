/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;
import com.mycompany.inventorymanagementapp.DTO.ReceiptDTO;
import com.mycompany.inventorymanagementapp.DTO.ReceiptItemDTO;
import com.mycompany.inventorymanagementapp.DTO.SupplierDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
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

    private ReceiptDTO convert(Document doc) {
        ReceiptDTO p = new ReceiptDTO();
        p.setNote(doc.getString("note"));
        p.setSupplierCode(doc.getString("supplierCode"));
        p.setReceiptDate(doc.getDate("receiptDate"));
        List<Document> itemDocs = (List<Document>) doc.get("items");

        List<ReceiptItemDTO> items = itemDocs.stream()
                .map(d -> new ReceiptItemDTO(
                d.getString("productCode"),
                d.getString("name"),
                d.getInteger("quantity"),
                d.getInteger("price")
        ))
                .toList();
        p.setItems(items);
        return p;
    }
}
