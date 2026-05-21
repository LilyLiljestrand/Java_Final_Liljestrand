package org.example.mccstudentrides.data;

import org.example.mccstudentrides.domain.Ride;
import org.example.mccstudentrides.domain.RideType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends CrudRepository<Ride, Integer>{
    Page<Ride> findRidesByCampusContainingIgnoreCase(RideType campus, Pageable pageable);
    Page<Ride> findRidesByIdContainingIgnoreCase(Integer id, Pageable pageable);
    Page<Ride> findAll(Pageable pageable);
}
