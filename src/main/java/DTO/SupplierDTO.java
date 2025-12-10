
package DTO;


public class SupplierDTO {
    
    private String supplierCode;
    private String name;
    private String email;
    private String address;
    private String phone;

 
    public SupplierDTO() {
    }

    public SupplierDTO(String supplierCode, String name, String email, String address, String phone) {
        this.supplierCode = supplierCode;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

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
    

    @Override
    public String toString() {
        return this.name;
    }
}