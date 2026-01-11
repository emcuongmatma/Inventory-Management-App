/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBConnection {

    private static MongoClient client;
    private static MongoDatabase database;

    private static final String URI = "mongodb+srv://nvc05042k4_db_user:Cuong2k4@inventorymanagementapp.fi3jgcs.mongodb.net/?appName=InventoryManagementApp";
    private static final String DB_NAME = "iventory_db";

    public static MongoDatabase getDatabase() {
        if (client == null) {
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(
                            PojoCodecProvider.builder()
                                    .register("com.mycompany.inventorymanagementapp.DTO")
                                    .automatic(true)
                                    .build()
                    )
            );
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URI))
                    .codecRegistry(pojoCodecRegistry)
                    .build();
            client = MongoClients.create(settings);
            database = client.getDatabase(DB_NAME).withCodecRegistry(pojoCodecRegistry);
        }
        return database;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}