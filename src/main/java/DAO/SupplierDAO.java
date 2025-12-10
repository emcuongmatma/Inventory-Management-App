/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import DTO.SupplierDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.bson.Document;

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
        for (Document doc : collection.find(Filters.regex("name", Pattern.compile(name, Pattern.CASE_INSENSITIVE)))) {
            list.add(convert(doc));
        }
        return list;
    }

    
    public boolean update(SupplierDTO s) {
        try {
            Document doc = new Document()
                    .append("name", s.getName())
                    .append("email", s.getEmail())
                    .append("address", s.getAddress())
                    .append("phone", s.getPhone());

            collection.updateOne(Filters.eq("supplierCode", s.getSupplierCode()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String supplierCode) {
        try {
            collection.deleteOne(Filters.eq("supplierCode", supplierCode));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNewSupplierCode() {
        String prefix = "SP";
        long number = collection.countDocuments();

        number++;

        String newNumber = String.format("%03d", number);
        return prefix + newNumber;
    }

    public SupplierDTO findByCode(String code) {
        Document doc = collection.find(Filters.eq("supplierCode", code)).first();
        if (doc != null) {
            return convert(doc);
        }
        return null;
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
