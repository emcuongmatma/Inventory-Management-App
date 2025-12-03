/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.OrderDAO;
import DAO.ProductDAO;
import com.mycompany.inventorymanagementapp.DTO.OrderDTO;
import com.mycompany.inventorymanagementapp.DTO.OrderItemDTO;
import com.mycompany.inventorymanagementapp.DTO.ProductDTO;
import java.util.Date;
import java.util.List;
import utils.OrderStatus;

/**
 *
 * @author ECMM
 */
public class OrderBUS {

    private static final OrderBUS instance = new OrderBUS();
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    public static OrderBUS getInstance() {
        return instance;
    }

    public void addNewOrder(String userId, String paymentMethod, OrderStatus status, List<OrderItemDTO> items) {
        Double totalAmount = 0.0;
        for (OrderItemDTO item : items) {
            List<ProductDTO> inventory = productDAO.findByCodeOrName(item.getProductCode());
            if (inventory.size() == 0) {
                System.out.println("Khong the tim thay san pham");
                return;
            }
            if (item.getQuantity() > inventory.getFirst().getQuantity()) {
                System.out.println("San pham trong kho khong du");
                return;
            }
            totalAmount += item.getPrice() * item.getQuantity();
        }
        OrderDTO order = new OrderDTO(userId, totalAmount, paymentMethod, status, new Date(), items);
        orderDAO.insert(order);
        for (OrderItemDTO item : items) {
            productDAO.decreaseQuantity(item.getProductCode(), item.getQuantity());
        }
    }

    public List<OrderDTO> findByUserId(String userId){
        return orderDAO.findByUserId(userId);
    }
    
    public List<OrderDTO> getAll(){
        return orderDAO.getAll();
    }
}
