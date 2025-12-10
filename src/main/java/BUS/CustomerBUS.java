package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;
import java.util.List;

public class CustomerBUS {

    private static final CustomerBUS instance = new CustomerBUS();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public static CustomerBUS getInstance() {
        return instance;
    }

    public boolean addNewUser(String name, String phone, String address) {
        List<CustomerDTO> exist = customerDAO.findByPhone(phone);
        if (!exist.isEmpty()) {
            return false; 
        }

        CustomerDTO customer = new CustomerDTO(name, phone, address);
        customerDAO.insert(customer);
        return true;
    }

    public List<CustomerDTO> findByPhone(String phone) {
        return customerDAO.findByPhone(phone);
    }
    
    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.getAll();
    }
}