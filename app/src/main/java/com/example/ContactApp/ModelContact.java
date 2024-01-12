package com.example.ContactApp;

public class ModelContact {

    private String id;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    private String name;
    private String Lastname;
    private String image;

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    private String phone;
    private String email;
    private String address;
    private String groupId;
    public ModelContact(String id, String name, String image){
        this.id = id;
        this.name = name;
        this.image = image;

    }
    public ModelContact(String id, String name, String image, String phone){
        this.id = id;
        this.name = name;
        this.image = image;
        this.phone = phone;
    }
    public ModelContact(String id, String name, String LastName, String image, String phone, String email, String address, String groupId) {
        this.id = id;
        this.name = name;
        this.Lastname = LastName;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
