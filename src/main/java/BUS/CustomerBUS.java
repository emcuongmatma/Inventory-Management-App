package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;
import java.util.List;

public class CustomerBUS {

    private static final CustomerBUS instance = new CustomerBUS();
    private CustomerDAO customerDAO = new CustomerDAO();

    public static CustomerBUS getInstance() {
        return instance;
    }

    // Sửa: Trả về boolean
    public boolean addNewUser(String name, String address, String phone) {
        // Kiểm tra trùng SĐT
        List<CustomerDTO> exist = customerDAO.findByPhone(phone);
        if (!exist.isEmpty()) {
            return false; // Đã tồn tại
        }
        
        CustomerDTO customer = new CustomerDTO(name, phone, address);
        customerDAO.insert(customer);
        return true;
    }

    public List<CustomerDTO> findByPhone(String phone) {
        return customerDAO.findByPhone(phone);
    }
    
    // Hàm mới cho GUI load danh sách
    public List<CustomerDTO> getAllCustomers() {
        return customerDAO.getAll();
    }
}