package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;

    public Ride getRide(Long id) {
        return getByIdOrElseThrow(id);
    }

    public List<Ride> gelAllRides() {
        return rideRepository.findAll();
    }

    public List<Ride> getAllCreatedRides() {
        return rideRepository.getRidesByStatusIs(RideStatus.CREATED);
    }

    public Ride createRide(Ride ride) {
        ride.setStatus(RideStatus.CREATED);
        ride.setDateTimeCreate(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    public Ride changeRideStatus(Long id, RideStatus status) {
        Ride ride = getByIdOrElseThrow(id);
        ride.setStatus(status);
        return rideRepository.save(ride);
    }

    private Ride getByIdOrElseThrow(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, id)));
    }
}
