
package DTO;


public class ReceiptItemDTO {
    private String productCode;
    private String name;
    private Integer quantity;
    private Integer unitPrice;

    public ReceiptItemDTO() {
    }
    public ReceiptItemDTO(String productCode, String name, Integer quantity, Integer unitPrice) {
        this.productCode = productCode;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

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