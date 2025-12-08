package DTO;

public class CategoryDTO {
    private String categoryCode;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String categoryCode, String name) {
        this.categoryCode = categoryCode;
        this.name = name;
    }

    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}