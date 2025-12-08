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

    
    public boolean addNewCategory(String categoryName) {
   
        List<CategoryDTO> list = categoryDAO.findByCodeOrName(categoryName);
        for (CategoryDTO c : list) {
            if (c.getName().equalsIgnoreCase(categoryName)) {
                return false; 
            }
        }

        CategoryDTO category = new CategoryDTO(categoryDAO.getNewCategoryCode(), categoryName);
        categoryDAO.insert(category);
        return true;
    }

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