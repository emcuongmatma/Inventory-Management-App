/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

/**
 *
 * @author ECMM
 */
public class CustomerDTO {
    // Các trường dữ liệu (dựa trên CustomerBUS và CustomerDAO)
    private String name;
    private String phone;
    private String address;

    // 1. Constructor rỗng (Bắt buộc)
    public CustomerDTO() {
    }

    // 2. Constructor đầy đủ (Khớp với CustomerBUS: new CustomerDTO(name, phone, adddress))
    public CustomerDTO(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // 3. Getters
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    // 4. Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}