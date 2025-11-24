/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mycompany.inventorymanagementapp.DTO.ProductDTO;
import config.MongoDBConnection;
import java.util.List;
import java.util.ArrayList;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
public class ProductDAO {

    private final MongoCollection<Document> collection;

    public ProductDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("products");
    }

    public void insert(ProductDTO p) {
        Document doc = new Document()
                .append("productCode", p.getProductCode())
                .append("name", p.getName())
                .append("description", p.getDescription())
                .append("quantity", p.getQuantity())
                .append("price", p.getPrice())
                .append("imageUrl", p.getImageUrl())
                .append("discountPercent", p.getDiscountPercent())
                .append("categoryCode", p.getCategoryCode())
                .append("brandCode", p.getBrandCode());
        collection.insertOne(doc);
    }

    public void update(ProductDTO p) {
        Document update = new Document("$set", new Document()
                .append("name", p.getName())
                .append("description", p.getDescription())
                .append("quantity", p.getQuantity())
                .append("price", p.getPrice())
                .append("imageUrl", p.getImageUrl())
                .append("discountPercent", p.getDiscountPercent())
                .append("categoryCode", p.getCategoryCode())
                .append("brandCode", p.getBrandCode())
        );

        collection.updateOne(eq("productCode", p.getProductCode()), update);
    }

    public void delete(String productCode) {
        collection.deleteOne(eq("productCode", productCode));
    }

    public List<ProductDTO> findByBrand(String brandCode) {
        List<ProductDTO> list = new ArrayList<>();
        for (Document doc : collection.find(eq("brandCode", brandCode))) {
            list.add(convert(doc));
        }
        return list;
    }

    public List<ProductDTO> findByCategory(String brandCode) {
        List<ProductDTO> list = new ArrayList<>();
        for (Document doc : collection.find(eq("categoryCode", brandCode))) {
            list.add(convert(doc));
        }
        return list;
    }

    public List<ProductDTO> findByCodeOrName(String keyword) {
        List<ProductDTO> list = new ArrayList<>();
        Document filter = new Document("$or", List.of(
                new Document("productCode", new Document("$regex", keyword).append("$options", "i")),
                new Document("name", new Document("$regex", keyword).append("$options", "i"))
        ));

        for (Document doc : collection.find(filter)) {
            list.add(convert(doc));
        }

        return list;
    }

    private ProductDTO convert(Document doc) {
        ProductDTO p = new ProductDTO();
        p.setProductCode(doc.getString("productCode"));
        p.setName(doc.getString("name"));
        p.setDescription(doc.getString("description"));
        p.setQuantity(doc.getInteger("quantity"));
        p.setPrice(doc.getInteger("price"));
        p.setImageUrl(doc.getString("imageUrl"));
        p.setDiscountPercent(doc.getDouble("discountPercent") != null
                ? doc.getDouble("discountPercent").floatValue()
                : null);
        p.setCategoryCode(doc.getString("categoryCode"));
        p.setBrandCode(doc.getString("brandCode"));
        return p;
    }
}
