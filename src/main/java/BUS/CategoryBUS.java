package BUS;

import DAO.CategoryDAO;
import DTO.CategoryDTO;
import java.util.List;

public class CategoryBUS {

    private static final CategoryBUS instance = new CategoryBUS();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public static CategoryBUS getInstance() {
        return instance;
    }

    public List<CategoryDTO> getAllCategory() {
        return categoryDAO.getAll();
    }

    // Sửa: Trả về boolean
    public boolean addNewCategory(String categoryName) {
        // 1. Kiểm tra trùng tên
        List<CategoryDTO> list = categoryDAO.findByCodeOrName(categoryName);
        for (CategoryDTO c : list) {
            if (c.getName().equalsIgnoreCase(categoryName)) {
                return false; // Đã tồn tại
            }
        }

        // 2. Thêm mới
        CategoryDTO category = new CategoryDTO(categoryDAO.getNewCategoryCode(), categoryName);
        categoryDAO.insert(category);
        return true;
    }
    
    // Sửa: Trả về boolean
    public boolean deleteCategory(String categoryCode){
        try {
            categoryDAO.delete(categoryCode);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<CategoryDTO> findCategoryByCodeOrName(String keyword){
        return categoryDAO.findByCodeOrName(keyword);
    }
}