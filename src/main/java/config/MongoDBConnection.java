/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author ECMM
 */
public class MongoDBConnection {

    private static MongoClient client;
    private static MongoDatabase database;

    private static final String URI = "mongodb+srv://nvc05042k4_db_user:Ke6t59apzNbt0o6e@inventorymanagementapp.fi3jgcs.mongodb.net/?appName=InventoryManagementApp";
    private static final String DB_NAME = "iventory_db";

    public static MongoDatabase getDatabase() {
        if (client == null) {
            client = MongoClients.create(URI);
            database = client.getDatabase(DB_NAME);
        }
        return database;
    }

    public static void close() {
        if (client != null) {
            client.close();
        }
    }
}
