package DTO;

public class BrandDTO {
    private String brandCode;
    private String name;

    public BrandDTO() {
    }

    public BrandDTO(String brandCode, String name) {
        this.brandCode = brandCode;
        this.name = name;
    }

    public String getBrandCode() { return brandCode; }
    public String getName() { return name; }

    public void setBrandCode(String brandCode) { this.brandCode = brandCode; }
    public void setName(String name) { this.name = name; }
}