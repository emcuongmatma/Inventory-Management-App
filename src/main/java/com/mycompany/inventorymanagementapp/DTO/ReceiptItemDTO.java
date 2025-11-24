/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementapp.DTO;
import lombok.Data;
/**
 *
 * @author ECMM
 */
@Data
public class ReceiptItemDTO {
    private String productCode;
    private String name;
    private Integer quantity;
    private Integer unitPrice;
}

