package BUS;

import DAO.SupplierDAO;
import DTO.SupplierDTO;
import java.util.List;

public class SupplierBUS {
    private final SupplierDAO supplierDAO;

    public SupplierBUS() {
        this.supplierDAO = new SupplierDAO();
    }

    // Lấy danh sách tất cả nhà cung cấp (tận dụng hàm findByName với chuỗi rỗng)
    public List<SupplierDTO> getAllSuppliers() {
        return supplierDAO.findByName("");
    }

    // Tìm kiếm theo tên
    public List<SupplierDTO> searchSupplier(String keyword) {
        return supplierDAO.findByName(keyword);
    }

    // Thêm nhà cung cấp mới
    public String addSupplier(SupplierDTO supplier) {
        // 1. Validate dữ liệu
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            return "Tên nhà cung cấp không được để trống!";
        }
        if (supplier.getPhone() == null || !supplier.getPhone().matches("\\d{10,11}")) {
            return "Số điện thoại không hợp lệ (phải là 10-11 số)!";
        }
        
        // 2. Nếu dữ liệu ổn, gọi DAO để insert
        try {
            supplierDAO.insert(supplier);
            return "Thêm nhà cung cấp thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi thêm nhà cung cấp: " + e.getMessage();
        }
    }

    // Lấy mã nhà cung cấp tự động từ DAO
    public String generateNewCode() {
        return supplierDAO.getNewSupplierCode();
    }
}