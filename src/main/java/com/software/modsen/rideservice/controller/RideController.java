package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
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

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @GetMapping
    public ResponseEntity<RideListResponse> getAllRides(){
        return ResponseEntity.ok().body(rideService.gelAllRides());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable Long id){
        return ResponseEntity.ok().body(rideService.getRide(id));
    }

    @GetMapping("/free")
    public ResponseEntity<RideListResponse> gelAllFreeRides(){
        return ResponseEntity.ok().body(rideService.getAllCreatedRides());
    }

    @PostMapping
    public ResponseEntity<RideResponse> createRide(@RequestBody RideRequest rideRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(rideService.createRide(rideRequest));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponse> cancelRide(@PathVariable Long id){
        return ResponseEntity.ok().body(rideService.cancelRide(id));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable Long id, @RequestBody Long driverId){
        return ResponseEntity.ok().body(rideService.acceptRide(id, driverId));
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<RideResponse> startRide(@PathVariable Long id){
        return ResponseEntity.ok().body(rideService.startRide(id));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<RideResponse> finishRide(@PathVariable Long id){
        return ResponseEntity.ok().body(rideService.finishRide(id));
    }
}
