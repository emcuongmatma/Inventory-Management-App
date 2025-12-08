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

    public boolean addNewBrand(String brandName) {
        List<BrandDTO> list = brandDAO.findByCodeOrName(brandName);
        for (BrandDTO b : list) {
            if (b.getName().equalsIgnoreCase(brandName)) {
                return false; // Đã tồn tại
            }
        }

        BrandDTO brandDTO = new BrandDTO(brandDAO.getNewBrandCode(), brandName);
        brandDAO.insert(brandDTO);
        return true;
    }

    public boolean deleteBrand(String brandCode) {
        
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