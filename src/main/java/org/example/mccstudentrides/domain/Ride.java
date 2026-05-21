package org.example.mccstudentrides.domain;

import org.example.mccstudentrides.api.RideDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class Ride implements Serializable{
    private static final long serialVersionUID = 1L;
    //
    @Id
    private int id;
    //
    //NotBlank(message = "name is required for passengers")
    private String name;
    //
    @NotBlank(message = "contact number is required for passengers")
    private String contact;

    private int classTime;
    //
    @Enumerated(EnumType.STRING)
    private RideType campus;
    //
    @Embedded
    private Image carPhoto;
    //
    @NotBlank(message = "name of car model is required for passengers")
    private String carModel;
    //
    @Digits(integer = 10, fraction = 0, message = "number of seats are required")
    private int seats;
    //
    @NotBlank(message = "end designation required")
    private String address;
    //
    @ManyToOne
    private User user;

    public Ride(){
        id = 0;
        name = " ";
        contact = " ";
        classTime = 0;
        campus = null;
        carPhoto = new Image();
        carModel = " ";
        seats = 0;
        address = " ";
    }

    public Ride(RideDTO dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.contact = dto.getContact();
        this.classTime = dto.getClassTime();
        this.campus = dto.getCampus();
        carPhoto = dto.getCarPhoto();
        this.carModel = dto.getCarModel();
        this.seats = dto.getSeats();
        this.address = dto.getAddress();
        user = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getClassTime() {
        return classTime;
    }

    public void setClassTime(int classTime) {
        this.classTime = classTime;
    }

    public RideType getCampus() {
        return campus;
    }

    public void setCampus(RideType campus) {
        this.campus = campus;
    }

    public Image getCarPhoto() {
        return carPhoto;
    }

    public void setCarPhoto(Image carPhoto) {
        this.carPhoto = carPhoto;
    }

    public String getCarModel() {
        return carModel;
    }

    public boolean hasCarPhoto(){
        return carPhoto != null && carPhoto.getImageContent().length > 0;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
