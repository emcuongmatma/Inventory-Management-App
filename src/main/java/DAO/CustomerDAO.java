/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mycompany.inventorymanagementapp.DTO.CustomerDTO;
import config.MongoDBConnection;
import java.util.List;
import java.util.ArrayList;
import org.bson.Document;


/**
 *
 * @author ECMM
 */
public class CustomerDAO {

    private final MongoCollection<Document> collection;

    public CustomerDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("customers");
    }

    public void insert(CustomerDTO customer) {
        Document doc = new Document()
                .append("name", customer.getName())
                .append("address", customer.getAddress())
                .append("phone", customer.getPhone());
        collection.insertOne(doc);
    }

    public List<CustomerDTO> findByPhone(String phone) {
        List<CustomerDTO> list = new ArrayList<>();
        for (Document doc : collection.find(eq("phone", phone))) {
            list.add(convert(doc));
        }
        return list;
    }

    private CustomerDTO convert(Document doc) {
        CustomerDTO p = new CustomerDTO();
        p.setName(doc.getString("name"));
        p.setPhone(doc.getString("phone"));
        p.setAddress(doc.getString("address"));
        return p;
    }
}
