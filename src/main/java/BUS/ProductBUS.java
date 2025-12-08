package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;
import java.util.List;

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

    public List<ProductDTO> getByCodeOrName(String keyword) {
        return productDAO.findByCodeOrName(keyword);
    }

    public boolean addProduct(ProductDTO product) {
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            product.setProductCode(productDAO.getNewProductCode());
        }

        if (productDAO.existsByCode(product.getProductCode())) {

            return increaseStock(product.getProductCode(), product.getQuantity());
        } else {

            productDAO.insert(product);
            return true;
        }
    }

    public void updateProduct(ProductDTO product){
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
        List<ProductDTO> list = productDAO.findByCodeOrName(productCode);
        if (list == null || list.isEmpty()) return false;
        
        ProductDTO product = list.get(0); 
        if (qty <= 0) return false;
        if (product.getQuantity() < qty) return false;

        return productDAO.decreaseQuantity(productCode, qty);
    }
}