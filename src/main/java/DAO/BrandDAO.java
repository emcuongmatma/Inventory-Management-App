
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts; 
import static com.mongodb.client.model.Filters.eq;
import DTO.BrandDTO;
import config.MongoDBConnection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;


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
        try {
            Document doc = collection.find().sort(Sorts.descending("brandCode")).first();

            if (doc == null) {
                return "BR001";
            }
            String lastCode = doc.getString("brandCode");
            int number = Integer.parseInt(lastCode.substring(2));

            number++;
            return String.format("BR%03d", number);
            
        } catch (Exception e) {
         
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