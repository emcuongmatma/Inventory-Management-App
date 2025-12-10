package BUS;

import DAO.SupplierDAO;
import DTO.SupplierDTO;
import java.util.List;

public class SupplierBUS {

    private final SupplierDAO supplierDAO;

    public SupplierBUS() {
        this.supplierDAO = new SupplierDAO();
    }

    public List<SupplierDTO> getAllSuppliers() {
        return supplierDAO.findByName("");
    }

    public List<SupplierDTO> searchSupplier(String keyword) {
        return supplierDAO.findByName(keyword);
    }

    public String updateSupplier(SupplierDTO s) {

        if (s.getName() == null || s.getName().trim().isEmpty()) {
            return "Tên nhà cung cấp không được để trống!";
        }

        if (supplierDAO.update(s)) {
            return "Cập nhật thành công!";
        } else {
            return "Cập nhật thất bại!";
        }
    }

    public String deleteSupplier(String code) {
        if (supplierDAO.delete(code)) {
            return "Xóa thành công!";
        } else {
            return "Xóa thất bại!";
        }
    }

    public String addSupplier(SupplierDTO supplier) {

        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            return "Tên nhà cung cấp không được để trống!";
        }
        if (supplier.getPhone() == null || !supplier.getPhone().matches("\\d{10,11}")) {
            return "Số điện thoại không hợp lệ (phải là 10-11 số)!";
        }

        if (supplierDAO.findByCode(supplier.getSupplierCode()) != null) {
            return "Lỗi: Mã nhà cung cấp " + supplier.getSupplierCode() + " đã tồn tại!";
        }

        try {
            supplierDAO.insert(supplier);
            return "Thêm nhà cung cấp thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi thêm nhà cung cấp: " + e.getMessage();
        }
    }

    public String generateNewCode() {
        return supplierDAO.getNewSupplierCode();
    }
}
