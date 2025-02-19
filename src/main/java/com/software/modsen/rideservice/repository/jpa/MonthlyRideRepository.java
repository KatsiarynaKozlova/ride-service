package com.software.modsen.rideservice.repository.jpa;

import com.software.modsen.rideservice.model.MonthlyRide;
import com.software.modsen.rideservice.model.MonthlyRideId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlyRideRepository extends JpaRepository<MonthlyRide, MonthlyRideId> {
    Optional<MonthlyRide> findByDriverIdAndMonthYear(Long driverId, String monthAndYear);
}
