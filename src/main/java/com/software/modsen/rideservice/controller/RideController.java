package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
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

import java.util.stream.Collectors;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;
    private final RideMapper rideMapper;

    @GetMapping
    public ResponseEntity<RideListResponse> getAllRides() {
        return ResponseEntity.ok(
                new RideListResponse(rideService.gelAllRides()
                        .stream()
                        .map(rideMapper::toResponse)
                        .collect(Collectors.toList())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
        return ResponseEntity.ok(rideMapper.toResponse(rideService.getRide(id)));
    }

    @GetMapping("/free")
    public ResponseEntity<RideListResponse> gelAllFreeRides() {
        return ResponseEntity.ok(
                new RideListResponse(rideService.getAllCreatedRides()
                        .stream()
                        .map(rideMapper::toResponse)
                        .collect(Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity<RideResponse> createRide(@RequestBody RideRequest rideRequest) {
        Ride ride = rideMapper.toModel(rideRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rideMapper.toResponse(rideService.createRide(ride)));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponse> cancelRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideMapper.toResponse(rideService.cancelRide(id)));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable Long id, @RequestBody Long driverId) {
        return ResponseEntity.ok(rideMapper.toResponse(rideService.acceptRide(id, driverId)));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<RideResponse> startRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideMapper.toResponse(rideService.startRide(id)));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<RideResponse> finishRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideMapper.toResponse(rideService.finishRide(id)));
    }
}
