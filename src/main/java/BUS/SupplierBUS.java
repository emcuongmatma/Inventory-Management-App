/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.SupplierDAO;
import com.mycompany.inventorymanagementapp.DTO.SupplierDTO;
import java.util.List;

/**
 *
 * @author ECMM
 */
public class SupplierBUS {
    private static final SupplierBUS instance = new SupplierBUS();
    private SupplierDAO supplierDAO = new SupplierDAO();

    public static SupplierBUS getInstance() {
        return instance;
    }
    
    public void addNewSupplier(String name, String phone, String email, String address){
        List<SupplierDTO> check = supplierDAO.findByNameOrCode(name);
        if (check.size() != 0) {
            System.out.println("Supplier already exists");
            return;
        }
        SupplierDTO supplier = new SupplierDTO(supplierDAO.getNewSupplierCode(), name, phone, email, address);
        supplierDAO.insert(supplier);
    }
    
        
    public List<SupplierDTO> findSupplierByCodeOrName(String keyword){
        return supplierDAO.findByNameOrCode(keyword);
    }
    
    public List<SupplierDTO> getAllSupplier(){
        return supplierDAO.getAll();
    }
}
