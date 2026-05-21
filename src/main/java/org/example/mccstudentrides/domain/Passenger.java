package org.example.mccstudentrides.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String passFullname;
    private int numPassenger;
    private String contact;
    private String address;

    @ManyToOne
    private Ride ride;

    public Passenger(int id, String passFullname, int numPassenger, String contact, String address){
        this.id = id;
        this.passFullname = passFullname;
        this.numPassenger = numPassenger;
        this.contact = contact;
        this.address= address;
    }



    /**public int getPassId() {
        return passId;
    }

    public void setPassId(int passId) {
        this.passId = passId;
    }

    public String getPassFullname() {
        return passFullname;
    }

    public void setPassFullname(String passFullname) {
        this.passFullname = passFullname;
    }

    public int getNumPassenger() {
        return numPassenger;
    }

    public void setNumPassenger(int numPassenger) {
        this.numPassenger = numPassenger;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }*/
}
