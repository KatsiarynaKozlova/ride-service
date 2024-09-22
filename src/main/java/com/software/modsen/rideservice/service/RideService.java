package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.Status;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.util.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final RideMapper rideMapper;

    public RideResponse getRide(Long id) {
        Ride ride = getByIdOrElseThrow(id);
        return rideMapper.toResponse(ride);
    }

    public RideListResponse gelAllRides() {
        return new RideListResponse(rideRepository.findAll().stream()
                .map(rideMapper::toResponse)
                .collect(Collectors.toList()));
    }

    public RideListResponse getAllCreatedRides() {
        return new RideListResponse(rideRepository.getRidesByStatusIs(Status.CREATED).stream()
                .map(rideMapper::toResponse)
                .collect(Collectors.toList()));
    }

    public RideResponse createRide(RideRequest request) {
        Ride ride = rideMapper.toModel(request);
        ride.setStatus(Status.CREATED);
        ride.setDateTimeCreate(LocalDateTime.now());
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    public RideResponse acceptRide(Long id, Long driverId) {
        Ride ride = getByIdOrElseThrow(id);
        ride.setDriverId(driverId);
        ride.setStatus(Status.ACCEPTED);
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    public RideResponse cancelRide(Long id) {
        Ride ride = getByIdOrElseThrow(id);
        ride.setStatus(Status.CANCELED);
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    public RideResponse finishRide(Long id) {
        Ride ride = getByIdOrElseThrow(id);
        ride.setStatus(Status.FINISHED);
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    public RideResponse startRide(Long id) {
        Ride ride = getByIdOrElseThrow(id);
        ride.setStatus(Status.ACTIVE);
        return rideMapper.toResponse(rideRepository.save(ride));
    }

    private Ride getByIdOrElseThrow(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, id)));
    }
}
