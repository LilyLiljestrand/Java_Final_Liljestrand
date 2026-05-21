package org.example.mccstudentrides.data;

import org.example.mccstudentrides.domain.Passenger;
import org.springframework.data.repository.CrudRepository;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {
    Iterable<Passenger> findByRideId(int rideId);
}

