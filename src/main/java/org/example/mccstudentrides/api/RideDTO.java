package org.example.mccstudentrides.api;

import org.example.mccstudentrides.domain.Ride;
import org.example.mccstudentrides.domain.Image;
import org.example.mccstudentrides.domain.RideType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RideDTO {
    private int id;
    private String name;
    private String contact;
    private int classTime;
    private RideType campus;
    private String carModel;
    private int seats;
    private String address;
    private Image carPhoto;
    private UserDTO user;

    public RideDTO(Ride ride){
        this.id = ride.getId();
        this.name = ride.getName();
        this.contact = ride.getContact();
        this.classTime = ride.getClassTime();
        this.campus = ride.getCampus();
        this.seats = ride.getSeats();
        this.address = ride.getAddress();
        this.carPhoto = ride.getCarPhoto();
        this.user = new UserDTO(ride.getUser());
    }
}
