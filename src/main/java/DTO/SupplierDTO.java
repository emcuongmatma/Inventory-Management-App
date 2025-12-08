/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author ECMM
 */
public class SupplierDTO {
    // Các trường dữ liệu
    private String supplierCode;
    private String name;
    private String email;
    private String address;
    private String phone;

    // 1. Constructor rỗng
    public SupplierDTO() {
    }

    // 2. Constructor đầy đủ
    public SupplierDTO(String supplierCode, String name, String email, String address, String phone) {
        this.supplierCode = supplierCode;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    // 3. Getters
    public String getSupplierCode() {
        return supplierCode;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    // 4. Setters
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    // Helper để hiển thị tên trong ComboBox (nếu cần dùng)
    @Override
    public String toString() {
        return this.name;
    }
}