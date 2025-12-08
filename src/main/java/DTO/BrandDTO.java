package DTO;

public class BrandDTO {
    private String brandCode;
    private String name;

    // Constructor mặc định (cần thiết cho MongoDB mapping)
    public BrandDTO() {
    }

    // Constructor đầy đủ (được dùng trong BUS)
    public BrandDTO(String brandCode, String name) {
        this.brandCode = brandCode;
        this.name = name;
    }

    // Getters
    public String getBrandCode() { return brandCode; }
    public String getName() { return name; }

    // Setters
    public void setBrandCode(String brandCode) { this.brandCode = brandCode; }
    public void setName(String name) { this.name = name; }
}