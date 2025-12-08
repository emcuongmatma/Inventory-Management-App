package DAO;

import DTO.AdminDTO;
import config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;

public class AdminDAO {
    private final MongoCollection<Document> collection;

    public AdminDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("admins"); // Tên collection là 'admins'
    }

    public AdminDTO checkLogin(String username, String password) {
        Document doc = collection.find(and(
                eq("userName", username),
                eq("password", password)
        )).first();

        if (doc != null) {
            return convert(doc);
        }
        return null;
    }

 
    public void createDefaultAdminIfEmpty() {
        if (collection.countDocuments() == 0) {
            Document doc = new Document()
                    .append("userName", "admin")
                    .append("password", "123456") 
                    .append("name", "Administrator")
                    .append("email", "admin@store.com")
                    .append("phone", "0000000000")
                    .append("address", "Hệ thống")
                    .append("gender", "Nam");
            collection.insertOne(doc);
            
        }
    }

    private AdminDTO convert(Document doc) {
        return new AdminDTO(
            doc.getString("userName"),
            doc.getString("name"),
            doc.getString("email"),
            doc.getString("password"),
            doc.getString("gender"),
            doc.getString("phone"),
            doc.getString("address")
        );
    }
}