package com.example.ritik_1;

public class UserProfileData {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String image;
    private String key;

    public UserProfileData(String name, String email, String phone, String address, String image) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image){this.image = image;}

    public UserProfileData(){

    }
}
