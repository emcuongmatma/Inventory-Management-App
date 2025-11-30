/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BUS;

import DAO.CustomerDAO;
import com.mycompany.inventorymanagementapp.DTO.CustomerDTO;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author ECMM
 */
public class CustomerBUS {

    private static final CustomerBUS instance = new CustomerBUS();
    private CustomerDAO customerDAO = new CustomerDAO();

    public static CustomerBUS getInstance() {
        return instance;
    }

    public void addNewUser(String name, String adddress, String phone) {
        List<CustomerDTO> doc = customerDAO.findByPhone(phone);
        if (doc.size() != 0) {
            System.out.println("The phone number .....");
            return;
        }
        CustomerDTO customer = new CustomerDTO(name, phone, adddress);
        customerDAO.insert(customer);
    }

    public List<CustomerDTO> findByPhone(String phone) {
        return customerDAO.findByPhone(phone);
    }
}
