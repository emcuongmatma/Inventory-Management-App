package BUS;

import DAO.BrandDAO;
import DTO.BrandDTO;
import java.util.List;

public class BrandBUS {

    private static final BrandBUS instance = new BrandBUS();
    private BrandDAO brandDAO = new BrandDAO();

    public static BrandBUS getInstance() {
        return instance;
    }

    public List<BrandDTO> getAllBrand() {
        return brandDAO.getAll();
    }

    // Sửa: Trả về boolean để GUI thông báo
    public boolean addNewBrand(String brandName) {
        // 1. Kiểm tra tên thương hiệu đã tồn tại chưa (Tìm chính xác)
        // Lưu ý: findByCodeOrName thường tìm gần đúng, nên ở đây ta check thủ công hoặc DAO cần hàm findByNameExact
        // Ở đây tạm dùng logic: Nếu tìm thấy tên trùng khớp 100% trong danh sách search thì báo trùng
        List<BrandDTO> list = brandDAO.findByCodeOrName(brandName);
        for (BrandDTO b : list) {
            if (b.getName().equalsIgnoreCase(brandName)) {
                return false; // Đã tồn tại
            }
        }

        // 2. Tạo mã mới và thêm
        BrandDTO brandDTO = new BrandDTO(brandDAO.getNewBrandCode(), brandName);
        brandDAO.insert(brandDTO);
        return true;
    }

    public boolean deleteBrand(String brandCode) {
        // Có thể thêm logic kiểm tra: Nếu Brand đang có sản phẩm thì không cho xóa
        // ProductBUS.getInstance().getByBrandCode(brandCode)...
        
        try {
            brandDAO.delete(brandCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<BrandDTO> findBrandByCodeOrName(String keyword){
        return brandDAO.findByCodeOrName(keyword);
    }
}