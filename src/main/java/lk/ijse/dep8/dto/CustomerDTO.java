package lk.ijse.dep8.dto;

import java.io.Serializable;

public class CustomerDTO implements Serializable {
    private String id;
    private String name;
    private String address;
    private byte[] image;

    public CustomerDTO() {
    }

    public CustomerDTO(String id, String name, String address, byte[] image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.image = image;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }




}
