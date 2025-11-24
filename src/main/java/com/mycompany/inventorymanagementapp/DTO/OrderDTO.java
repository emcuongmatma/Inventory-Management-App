/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementapp.DTO;

import java.util.List;
import lombok.Data;
import utils.OrderStatus;

/**
 *
 * @author ECMM
 */
@Data
public class OrderDTO {
    private Integer userId;
    private Long totalAmount;
    private String paymentMethod;
    private OrderStatus status;
    private List<OrderItemDTO> items;
}
