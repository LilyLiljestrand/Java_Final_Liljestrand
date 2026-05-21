package org.example.mccstudentrides.messaging;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface EffectedRideRepository extends CrudRepository<EffectedRide, Integer>{
    boolean existsByRideId(int rideId);
    Optional<EffectedRide> findbyRideId(int rideId);
}
