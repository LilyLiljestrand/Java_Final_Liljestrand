package org.example.mccstudentrides.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffectedRideAlert {
    private int rideId;
    private String passName;
    private String rideContact;
    private String rideAddress;
    private int remainingSeats;
    private boolean cancelled;

    @Override
    public String toString(){
        if (cancelled) {
            return "Ride ID: " + rideId + "\nPassenger: " + passName + "\nThe driver has Cancelled this ride.";
        }

        if (remainingSeats == 0) {
            return "Ride ID: " + rideId + "\nPassenger: " + passName + "\nDestination: " + rideAddress +
                    "\nThere are No More Seats on this rude.";
        }

        return "Ride ID: " + rideId + "\nPassenger: " + passName + "\nDestination: " + rideAddress +
                "\nSeats remaining: " + remainingSeats;
    }
}
