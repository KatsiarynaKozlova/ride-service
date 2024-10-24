package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.util.ExceptionMessages;
import com.software.modsen.rideservice.util.LogInfoMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;

    public Ride getRide(Long id) {
        Ride ride = getByIdOrElseThrow(id);
        log.info(String.format(LogInfoMessages.GET_RIDE_BY_ID, id));
        return ride;
    }

    public List<Ride> gelAllRides() {
        List<Ride> rides = rideRepository.findAll();
        log.info(LogInfoMessages.GET_LIST_OF_RIDES);
        return rides;
    }

    public List<Ride> getAllCreatedRides() {
        List<Ride> rides = rideRepository.getRidesByStatusIs(RideStatus.CREATED);
        log.info(LogInfoMessages.GET_LIST_OF_CREATED_RIDES);
        return rides;
    }

    public Ride createRide(Ride ride) {
        ride.setStatus(RideStatus.CREATED);
        ride.setCreatedAt(LocalDateTime.now());
        Ride newRide = rideRepository.save(ride);
        log.info(String.format(LogInfoMessages.CREATE_RIDE_WITH_ID, newRide.getId()));
        return newRide;
    }

    @Transactional
    public Ride changeRideStatus(Long id, RideStatus status) {
        Ride ride = getByIdLockedOrELseThrow(id);
        ride.setStatus(status);
        Ride updatedRide = rideRepository.save(ride);
        log.info(String.format(LogInfoMessages.UPDATE_RIDE_STATUS, updatedRide.getId()));
        return updatedRide;
    }

    private Ride getByIdOrElseThrow(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, id)));
    }

    private Ride getByIdLockedOrELseThrow(Long id) {
        return rideRepository.findByIdLocked(id)
                .orElseThrow(() -> new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, id)));
    }
}
