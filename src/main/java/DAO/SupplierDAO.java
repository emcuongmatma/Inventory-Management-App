/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mycompany.inventorymanagementapp.DTO.SupplierDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
public class SupplierDAO {

    private final MongoCollection<Document> collection;

    public SupplierDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("suppliers");
    }

    public void insert(SupplierDTO supplier) {
        Document doc = new Document()
                .append("supplierCode", supplier.getSupplierCode())
                .append("name", supplier.getName())
                .append("email", supplier.getEmail())
                .append("address", supplier.getAddress())
                .append("phone", supplier.getPhone());
        collection.insertOne(doc);
    }

    public List<SupplierDTO> findByNameOrCode(String keyword) {
        List<SupplierDTO> list = new ArrayList<>();
        Document filter = new Document("$or", List.of(
                new Document("supplierCode", new Document("$regex", keyword).append("$options", "i")),
                new Document("name", new Document("$regex", keyword).append("$options", "i"))
        ));

        for (Document doc : collection.find(filter)) {
            list.add(convert(doc));
        }
        return list;
    }

    public List<SupplierDTO> getAll() {
        List<SupplierDTO> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(convert(doc));
        }
        return list;
    }
    
    public String getNewSupplierCode() {
        String prefix = "SP";
        long number = collection.countDocuments();

        number++;

        String newNumber = String.format("%03d", number);
        return prefix + newNumber;
    }

    private SupplierDTO convert(Document doc) {
        SupplierDTO p = new SupplierDTO();
        p.setName(doc.getString("name"));
        p.setPhone(doc.getString("phone"));
        p.setSupplierCode(doc.getString("supplierCode"));
        p.setEmail(doc.getString("email"));
        p.setAddress(doc.getString("address"));
        return p;
    }
}
