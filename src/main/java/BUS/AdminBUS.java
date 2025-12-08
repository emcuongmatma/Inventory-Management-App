package BUS;

import DAO.AdminDAO;
import DTO.AdminDTO;

public class AdminBUS {
    private static final AdminBUS instance = new AdminBUS();
    private AdminDAO adminDAO = new AdminDAO();

    public static AdminBUS getInstance() {
        return instance;
    }

    public AdminBUS() {
      
        adminDAO.createDefaultAdminIfEmpty();
    }

    public AdminDTO login(String username, String password) {
        if(username == null || password == null) return null;
        return adminDAO.checkLogin(username, password);
    }
}