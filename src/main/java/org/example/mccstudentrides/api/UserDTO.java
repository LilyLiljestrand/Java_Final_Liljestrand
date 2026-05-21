package org.example.mccstudentrides.api;

import org.example.mccstudentrides.domain.RideType;
import org.example.mccstudentrides.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String fullname;
    private String contact;
    private RideType campus;
    private String address;
    private String city;
    private String state;

    public UserDTO(User user){
        this.id = user.getUserId();
        this.fullname = user.getFullname();
        this.contact = user.getContact();
        this.campus = user.getCampus();
        this.address = user.getAddress();
        this.city = user.getCity();
        this.state = user.getState();
    }
}
