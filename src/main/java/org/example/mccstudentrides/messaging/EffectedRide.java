package org.example.mccstudentrides.messaging;

import org.example.mccstudentrides.domain.Ride;
//import org.mccstudentrides.demo.domain.Passenger
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffectedRide {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int rideId;

    @ManyToOne
    Ride ridePass;

    /**@ManyToOne
    Passenger passRide;*/

}
