/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.ProductDAO;
import com.mycompany.inventorymanagementapp.DTO.ProductDTO;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ECMM
 */
public class ProductBUS {
    private static final ProductBUS instance = new ProductBUS();
    private ProductDAO productDAO = new ProductDAO();

    public static ProductBUS getInstance() {
        return instance;
    }

    public List<ProductDTO> getAllProducts() {
        return productDAO.getAll();
    }

    public List<ProductDTO> getByBrandCode(String brandCode) {
        return productDAO.findByBrand(brandCode);
    }

    public List<ProductDTO> getByCategory(String categoryCode) {
        return productDAO.findByCategory(categoryCode);
    }

    public List<ProductDTO> getByCodeOrName(String code) {
        return productDAO.findByCodeOrName(code);
    }

    public boolean addProduct(ProductDTO product) {
        //check valid truoc
        
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            product.setProductCode(productDAO.getNewProductCode());
        }

        if (productDAO.existsByCode(product.getProductCode())) {
            increaseStock(product.getProductCode(), product.getQuantity());
        } else {
            productDAO.insert(product);
        }
        return true;
    }
    
    public void updateProduct(ProductDTO product){
        //check valid truoc
        productDAO.update(product);
    }
    
    public void deleteProduct(String productCode){
        productDAO.delete(productCode);
    }
    
    public boolean increaseStock(String productCode, int qty) {
        if (qty <= 0) return false;
        return productDAO.increaseQuantity(productCode, qty);
    }

    public boolean decreaseStock(String productCode, int qty) {
        ProductDTO product = productDAO.findByCodeOrName(productCode).getFirst();

        if (product == null) return false;
        if (qty <= 0) return false;
        if (product.getQuantity() < qty) return false;

        return productDAO.decreaseQuantity(productCode, qty);
    }
}
