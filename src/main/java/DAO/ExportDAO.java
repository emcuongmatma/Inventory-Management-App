package DAO;

import DTO.ExportDTO;
import DTO.ReceiptItemDTO;
import config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class ExportDAO {
    private final MongoCollection<Document> collection;

    public ExportDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("exports");
    }
public List<ExportDTO> findByDateRange(Date start, Date end) {
    List<ExportDTO> list = new ArrayList<>();
    // Lọc: exportDate >= start AND exportDate <= end
    Bson filter = Filters.and(
        Filters.gte("exportDate", start),
        Filters.lte("exportDate", end)
    );

    // Sắp xếp ngày gần nhất lên đầu
    for (Document doc : collection.find(filter).sort(Sorts.descending("exportDate"))) {
        list.add(convert(doc));
    }
    return list;
}
    public void insert(ExportDTO export) {
        List<Document> itemsDoc = new ArrayList<>();
        for (ReceiptItemDTO item : export.getItems()) {
            itemsDoc.add(new Document()
                .append("productCode", item.getProductCode())
                .append("name", item.getName())
                .append("quantity", item.getQuantity())
                .append("unitPrice", item.getUnitPrice()));
        }

        Document doc = new Document()
                .append("customerCode", export.getCustomerCode())
                .append("note", export.getNote())
                .append("exportDate", export.getExportDate())
                .append("totalPrice", export.getTotalPrice())
                .append("items", itemsDoc);
                
        collection.insertOne(doc);
    }

    public List<ExportDTO> getAll() {
        List<ExportDTO> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            List<Document> itemsDoc = (List<Document>) doc.get("items");
            List<ReceiptItemDTO> items = new ArrayList<>();
            if (itemsDoc != null) {
                for (Document itemDoc : itemsDoc) {
                    items.add(new ReceiptItemDTO(
                        itemDoc.getString("productCode"),
                        itemDoc.getString("name"),
                        itemDoc.getInteger("quantity"),
                        itemDoc.getInteger("unitPrice")
                    ));
                }
            }
            
            list.add(new ExportDTO(
                doc.getObjectId("_id"),
                doc.getString("customerCode"),
                doc.getString("note"),
                doc.getDate("exportDate"),
                doc.getDouble("totalPrice"),
                items
            ));
        }
        return list;
    }
    // ==========================================================
    // BẠN ĐANG THIẾU ĐOẠN CODE NÀY TRONG ExportDAO.java
    // ==========================================================
    
    private ExportDTO convert(Document doc) {
        ExportDTO e = new ExportDTO();
        
        // 1. Lấy ID
        e.set_id(doc.getObjectId("_id"));
        
        // 2. Lấy thông tin cơ bản
        e.setCustomerCode(doc.getString("customerCode"));
        e.setNote(doc.getString("note"));
        e.setExportDate(doc.getDate("exportDate"));
        
        // 3. Lấy tổng tiền (Xử lý null để tránh lỗi)
        // Lưu ý: MongoDB đôi khi lưu là Integer, đôi khi Double, nên dùng getDouble an toàn hơn
        Double total = doc.getDouble("totalPrice");
        e.setTotalPrice(total != null ? total : 0.0);

        // 4. Lấy danh sách sản phẩm (items)
        // Vì ExportDTO dùng chung ReceiptItemDTO cho chi tiết sp
        List<org.bson.Document> itemDocs = (List<org.bson.Document>) doc.get("items");
        if (itemDocs != null) {
            List<DTO.ReceiptItemDTO> items = new java.util.ArrayList<>();
            for (org.bson.Document itemDoc : itemDocs) {
                // Đảm bảo tên trường khớp với lúc bạn insert (save)
                // Giả sử lúc lưu bạn dùng: productCode, name, quantity, price
                items.add(new DTO.ReceiptItemDTO(
                    itemDoc.getString("productCode"),
                    itemDoc.getString("name"),
                    itemDoc.getInteger("quantity", 0),
                    itemDoc.getInteger("price", 0) // Hoặc getInteger("unitPrice") tùy lúc lưu
                ));
            }
            e.setItems(items);
        }
        
        return e;
    }
}