/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.CategoryDAO;
import DAO.ProductDAO;
import com.mycompany.inventorymanagementapp.DTO.CategoryDTO;
import java.util.List;

/**
 *
 * @author ECMM
 */
public class CategoryBUS {

    private static final CategoryBUS instance = new CategoryBUS();
    private CategoryDAO categoryDAO = new CategoryDAO();

    public static CategoryBUS getInstance() {
        return instance;
    }

    public void addNewCategory(String categoryName) {
        List<CategoryDTO> result = categoryDAO.findByCodeOrName(categoryName);
        if (result.size() != 0) {
            System.out.println("Category already exist");
            return;
        }
        CategoryDTO category = new CategoryDTO(categoryDAO.getNewCategoryCode(), categoryName);
        categoryDAO.insert(category);
    }
    
    public void deleteCategory(String categoryCode){
        categoryDAO.delete(categoryCode);
    }
    
    public List<CategoryDTO> findCategoryByCodeOrName(String keyword){
        return categoryDAO.findByCodeOrName(keyword);
    }

    public List<CategoryDTO> getAllCategory() {
        return categoryDAO.getAll();
    }
    
    
}
