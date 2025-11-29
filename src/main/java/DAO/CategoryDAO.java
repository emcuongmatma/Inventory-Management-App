/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mycompany.inventorymanagementapp.DTO.CategoryDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
public class CategoryDAO {

    private final MongoCollection<Document> collection;

    public CategoryDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("categories");
    }

    public void insert(CategoryDTO category) {
        Document doc = new Document()
                .append("name", category.getName())
                .append("categoryCode", category.getCategoryCode());
        collection.insertOne(doc);
    }

    public void update(CategoryDTO category) {
        Document update = new Document("$set", new Document()
                .append("name", category.getName())
                .append("categoryCode", category.getCategoryCode())
        );

        collection.updateOne(eq("categoryCode", category.getCategoryCode()), update);
    }

    public void delete(String categoryCode) {
        collection.deleteOne(eq("categoryCode", categoryCode));
    }

    public List<CategoryDTO> findByCodeOrName(String keyword) {
        List<CategoryDTO> list = new ArrayList<>();
        Document filter = new Document("$or", List.of(
                new Document("categoryCode", new Document("$regex", keyword).append("$options", "i")),
                new Document("name", new Document("$regex", keyword).append("$options", "i"))
        ));

        for (Document doc : collection.find(filter)) {
            list.add(convert(doc));
        }

        return list;
    }

    public List<CategoryDTO> getAll() {
        List<CategoryDTO> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(convert(doc));
        }
        return list;
    }

    public String getNewBrandCode() {
        String prefix = "CT";
        long number = collection.countDocuments();

        number++;

        String newNumber = String.format("%03d", number);
        return prefix + newNumber;
    }

    private CategoryDTO convert(Document doc) {
        CategoryDTO p = new CategoryDTO();
        p.setName(doc.getString("name"));
        p.setCategoryCode(doc.getString("categoryCode"));
        return p;
    }
}
