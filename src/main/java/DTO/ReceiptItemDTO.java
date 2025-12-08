/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author ECMM
 */
public class ReceiptItemDTO {
    private String productCode;
    private String name;
    private Integer quantity;
    private Integer unitPrice;

    // 1. Constructor rỗng (Bắt buộc cho thư viện MongoDB)
    public ReceiptItemDTO() {
    }

    // 2. Constructor đầy đủ
    public ReceiptItemDTO(String productCode, String name, Integer quantity, Integer unitPrice) {
        this.productCode = productCode;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // 3. Getters
    public String getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    // 4. Setters
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}