package org.example.mccstudentrides.messaging;

import org.example.mccstudentrides.data.PassengerRepository;
import org.example.mccstudentrides.data.RideRepository;
import org.example.mccstudentrides.domain.Passenger;
import org.example.mccstudentrides.domain.Ride;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepo;
    private final RideRepository rideRepo;

    public PassengerService(PassengerRepository passengerRepo, RideRepository rideRepo) {
        this.passengerRepo = passengerRepo;
        this.rideRepo = rideRepo;
    }

    public Passenger addPassenger(int id, Passenger passenger) {
        Ride ride = rideRepo.findById(id).orElseThrow();

        if (ride.getSeats() < passenger.getNumPassenger()) {
            throw new IllegalStateException("Not enough seats available");
        }

        ride.setSeats(ride.getSeats() - passenger.getNumPassenger());
        passenger.setRide(ride);

        rideRepo.save(ride);
        return passengerRepo.save(passenger);
    }
}
