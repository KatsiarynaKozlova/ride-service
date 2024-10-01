package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;
    private final RideMapper rideMapper;

    @GetMapping
    public ResponseEntity<RideListResponse> getAllRides() {
        List<Ride> rideList = rideService.gelAllRides();
        List<RideResponse> rideResponseList = rideMapper.toRideResponseList(rideList);
        return ResponseEntity.ok(new RideListResponse(rideResponseList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
        Ride ride = rideService.getRide(id);
        RideResponse rideResponse = rideMapper.toResponse(ride);
        return ResponseEntity.ok(rideResponse);
    }

    @GetMapping("/free")
    public ResponseEntity<RideListResponse> gelAllFreeRides() {
        List<Ride> rideList = rideService.getAllCreatedRides();
        List<RideResponse> rideResponseList = rideMapper.toRideResponseList(rideList);
        return ResponseEntity.ok(new RideListResponse(rideResponseList));
    }

    @PostMapping
    public ResponseEntity<RideResponse> createRide(@RequestBody RideRequest rideRequest) {
        Ride ride = rideMapper.toModel(rideRequest);
        Ride newRide = rideService.createRide(ride);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rideMapper.toResponse(newRide));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RideResponse> changeRideStatus(@PathVariable Long id, @RequestBody RideStatus status) {
        Ride ride = rideService.changeRideStatus(id, status);
        return ResponseEntity.ok(rideMapper.toResponse(ride));
    }
}
