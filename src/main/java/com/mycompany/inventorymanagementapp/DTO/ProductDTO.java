/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementapp.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author ECMM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productCode;
    private String name;
    private String description;
    private Integer quantity;
    private Integer price;
    private String imageUrl;
    private Float discountPercent;
    private String categoryCode;
    private String brandCode;
    
}
