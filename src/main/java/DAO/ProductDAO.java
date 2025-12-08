package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts; // QUAN TRỌNG: Import cái này
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import DTO.ProductDTO;
import config.MongoDBConnection;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.bson.Document;

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
                .append("description", p.getDescription()) // Nếu null thì MongoDB tự bỏ qua hoặc lưu null
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

    public List<ProductDTO> getAll() {
        List<ProductDTO> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(convert(doc));
        }
        return list;
    }

    // --- CÁC HÀM TÌM KIẾM ---
    public List<ProductDTO> findByBrand(String brandCode) {
        List<ProductDTO> list = new ArrayList<>();
        for (Document doc : collection.find(eq("brandCode", brandCode))) {
            list.add(convert(doc));
        }
        return list;
    }

    public List<ProductDTO> findByCategory(String categoryCode) {
        List<ProductDTO> list = new ArrayList<>();
        for (Document doc : collection.find(eq("categoryCode", categoryCode))) {
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

    // --- HÀM SINH MÃ TỰ ĐỘNG CHUẨN XÁC ---
    public String getNewProductCode() {
        try {
            // Lấy sản phẩm có mã lớn nhất (Sắp xếp giảm dần theo productCode)
            Document doc = collection.find().sort(Sorts.descending("productCode")).first();
            
            if (doc == null) {
                return "PD001"; // Nếu chưa có SP nào
            }
            
            String lastCode = doc.getString("productCode");
            // Tách phần số: PD005 -> 5. Bỏ 2 ký tự đầu "PD"
            int number = Integer.parseInt(lastCode.substring(2));
            number++;
            
            return String.format("PD%03d", number);
        } catch (Exception e) {
            // Fallback nếu dữ liệu cũ sai định dạng
            return "PD" + System.currentTimeMillis();
        }
    }

    private ProductDTO convert(Document doc) {
        ProductDTO p = new ProductDTO();
        p.setProductCode(doc.getString("productCode"));
        p.setName(doc.getString("name"));
        p.setDescription(doc.getString("description"));
        // Sử dụng getInteger(key, defaultValue) để tránh lỗi null
        p.setQuantity(doc.getInteger("quantity", 0));
        p.setPrice(doc.getInteger("price", 0));
        p.setImageUrl(doc.getString("imageUrl"));
        
        Double discount = doc.getDouble("discountPercent");
        p.setDiscountPercent(discount != null ? discount.floatValue() : 0f);
        
        p.setCategoryCode(doc.getString("categoryCode"));
        p.setBrandCode(doc.getString("brandCode"));
        return p;
    }

    public boolean existsByCode(String productCode) {
        return collection.countDocuments(eq("productCode", productCode)) > 0;
    }

    public boolean increaseQuantity(String productCode, int qty) {
        UpdateResult result = collection.updateOne(
                eq("productCode", productCode),
                Updates.inc("quantity", qty)
        );
        return (result.getModifiedCount() > 0L);
    }

    public boolean decreaseQuantity(String productCode, int qty) {
        Document updated = collection.findOneAndUpdate(
                eq("productCode", productCode),
                Updates.inc("quantity", -qty)
        );
        return updated != null;
    }
}