/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.BrandDAO;
import com.mycompany.inventorymanagementapp.DTO.BrandDTO;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
public class BrandBUS {

    private static final BrandBUS instance = new BrandBUS();
    private BrandDAO brandDAO = new BrandDAO();

    public static BrandBUS getInstance() {
        return instance;
    }

    public void addNewBrand(String brandName) {
        List<BrandDTO> result = brandDAO.findByCodeOrName(brandName);
        if (result.size() != 0) {
            System.out.println("Brand already exist");
            return;
        }
        BrandDTO brandDTO = new BrandDTO(brandDAO.getNewBrandCode(), brandName);
        brandDAO.insert(brandDTO);
    }

//    public void updateBrandInfo(String brandCode, String brandName) {
//        BrandDTO brandDTO = new BrandDTO(brandCode, brandName);
//        brandDAO.update(brandDTO);
//    }
    
    public void deleteBrand(String brandCode){
        //check hoac lam gi do ....
        brandDAO.delete(brandCode);
    }
    
    public List<BrandDTO> findBrandByCodeOrName(String keyword){
        return brandDAO.findByCodeOrName(keyword);
    }
    
    public List<BrandDTO> getAllBrand(){
        return brandDAO.getAll();
    }
}
