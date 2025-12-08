/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts; // Quan trọng: Import để sắp xếp
import static com.mongodb.client.model.Filters.eq;
import DTO.BrandDTO;
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
    
    // --- HÀM ĐÃ SỬA: Logic sinh mã tự tăng chuẩn xác ---
    public String getNewBrandCode() {
        try {
            // 1. Tìm brand có mã lớn nhất (Sắp xếp giảm dần theo brandCode)
            Document doc = collection.find().sort(Sorts.descending("brandCode")).first();
            
            // 2. Nếu chưa có dữ liệu nào -> Trả về BR001
            if (doc == null) {
                return "BR001";
            }
            
            // 3. Lấy mã hiện tại (ví dụ: BR005)
            String lastCode = doc.getString("brandCode");
            
            // 4. Tách phần số (Bỏ 2 ký tự đầu là "BR") -> lấy "005" -> parse thành số 5
            int number = Integer.parseInt(lastCode.substring(2));
            
            // 5. Tăng lên 1 -> thành 6
            number++;
            
            // 6. Format lại thành chuỗi BR006
            return String.format("BR%03d", number);
            
        } catch (Exception e) {
            // Phòng trường hợp dữ liệu cũ không đúng định dạng BRxxx
            return "BR" + (collection.countDocuments() + 1);
        }
    }
    
     private BrandDTO convert(Document doc) {
        BrandDTO p = new BrandDTO();
        p.setName(doc.getString("name"));
        p.setBrandCode(doc.getString("brandCode"));
        return p;
    }
}