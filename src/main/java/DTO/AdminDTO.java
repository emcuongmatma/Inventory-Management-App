package DTO;

public class AdminDTO {
    private String userName;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;

    public AdminDTO() {
    }

    public AdminDTO(String userName, String name, String email, String password, String gender, String phone, String address) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }


    public String getUserName() { return userName; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public void setUserName(String userName) { this.userName = userName; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setGender(String gender) { this.gender = gender; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
}