/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mycompany.inventorymanagementapp.DTO.SupplierDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
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

    public List<SupplierDTO> findByName(String name) {
        List<SupplierDTO> list = new ArrayList<>();
        for (Document doc : collection.find(eq("name", name))) {
            list.add(convert(doc));
        }
        return list;
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
