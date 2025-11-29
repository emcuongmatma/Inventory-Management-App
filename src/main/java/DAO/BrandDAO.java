/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mycompany.inventorymanagementapp.DTO.BrandDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
public class BrandDAO {
    private final MongoCollection<Document> collection;

    public BrandDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("brands");
    }
    
    public void insert(BrandDTO brand) {
        Document doc = new Document()
                .append("name", brand.getName())
                .append("brandCode", brand.getBrandCode());
        collection.insertOne(doc);
    }

    public void update(BrandDTO brand) {
        Document update = new Document("$set", new Document()
                .append("name", brand.getName())
                .append("brandCode", brand.getBrandCode())
        );

        collection.updateOne(eq("brandCode", brand.getBrandCode()), update);
    }

    public void delete(String brandCode) {
        collection.deleteOne(eq("brandCode", brandCode));
    }

     public List<BrandDTO> findByCodeOrName(String keyword) {
        List<BrandDTO> list = new ArrayList<>();
        Document filter = new Document("$or", List.of(
                new Document("brandCode", new Document("$regex", keyword).append("$options", "i")),
                new Document("name", new Document("$regex", keyword).append("$options", "i"))
        ));

        for (Document doc : collection.find(filter)) {
            list.add(convert(doc));
        }

        return list;
    }
    public List<BrandDTO> getAll() {
        List<BrandDTO> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(convert(doc));
        }
        return list;
    }
    
    public String getNewBrandCode() {
        String prefix = "BR";
        long number = collection.countDocuments();

        number++;

        String newNumber = String.format("%03d", number);
        return prefix + newNumber;
    }
    
     private BrandDTO convert(Document doc) {
        BrandDTO p = new BrandDTO();
        p.setName(doc.getString("name"));
        p.setBrandCode(doc.getString("brandCode"));
        return p;
    }


}
