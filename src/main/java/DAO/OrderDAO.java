/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import DTO.OrderDTO;
import config.MongoDBConnection;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author ECMM
 */
public class OrderDAO {
    private final MongoCollection<OrderDTO> collection;

    public OrderDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("orders",OrderDTO.class);
    }

    public void insert(OrderDTO order) {
        collection.insertOne(order);
    }

    public List<OrderDTO> getAll() {
        List<OrderDTO> list = new ArrayList<>();
        for (OrderDTO order : collection.find().sort(Sorts.descending("orderDate"))) {
            list.add(order);
        }
        return list;
    }
}