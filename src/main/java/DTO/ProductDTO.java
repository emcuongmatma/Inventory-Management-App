package DTO;

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

    public ProductDTO() {
    }

    public ProductDTO(String productCode, String name, String description, Integer quantity, Integer price, String imageUrl, Float discountPercent, String categoryCode, String brandCode) {
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.imageUrl = imageUrl;
        this.discountPercent = discountPercent;
        this.categoryCode = categoryCode;
        this.brandCode = brandCode;
    }

    public String getProductCode() { return productCode; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Integer getQuantity() { return quantity; }
    public Integer getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public Float getDiscountPercent() { return discountPercent; }
    public String getCategoryCode() { return categoryCode; }
    public String getBrandCode() { return brandCode; }

    public void setProductCode(String productCode) { this.productCode = productCode; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setPrice(Integer price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDiscountPercent(Float discountPercent) { this.discountPercent = discountPercent; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public void setBrandCode(String brandCode) { this.brandCode = brandCode; }
}