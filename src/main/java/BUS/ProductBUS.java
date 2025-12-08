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

    // Chức năng: Thêm mới hoặc Tăng tồn kho nếu đã tồn tại
    public boolean addProduct(ProductDTO product) {
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            product.setProductCode(productDAO.getNewProductCode());
        }

        if (productDAO.existsByCode(product.getProductCode())) {
            // Nếu đã tồn tại -> Tăng số lượng (Chức năng nhập kho)
            return increaseStock(product.getProductCode(), product.getQuantity());
        } else {
            // Chưa tồn tại -> Thêm mới
            productDAO.insert(product);
            return true;
        }
    }
    
    // Chức năng: Sửa thông tin sản phẩm
    public void updateProduct(ProductDTO product){
        productDAO.update(product);
    }
    
    // Chức năng: Xóa sản phẩm
    public void deleteProduct(String productCode){
        productDAO.delete(productCode);
    }
    
    // Tăng tồn kho
    public boolean increaseStock(String productCode, int qty) {
        if (qty <= 0) return false;
        return productDAO.increaseQuantity(productCode, qty);
    }

    // Giảm tồn kho (Logic đã fix an toàn hơn)
    public boolean decreaseStock(String productCode, int qty) {
        List<ProductDTO> list = productDAO.findByCodeOrName(productCode);
        if (list == null || list.isEmpty()) return false;
        
        ProductDTO product = list.get(0); // Lấy phần tử đầu tiên an toàn
        if (qty <= 0) return false;
        if (product.getQuantity() < qty) return false; // Không đủ hàng để giảm

        return productDAO.decreaseQuantity(productCode, qty);
    }
}