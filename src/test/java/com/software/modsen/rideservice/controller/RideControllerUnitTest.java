package com.software.modsen.rideservice.controller;


import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.service.RideService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RideControllerUnitTest {
    @InjectMocks
    private RideController rideController;
    @Mock
    private RideService rideService;
    @Mock
    private RideMapper rideMapper;

    @Test
    public void testGetAllRides() {
        List<Ride> rideList = Collections.singletonList(new Ride());
        when(rideService.gelAllRides()).thenReturn(rideList);
        when(rideMapper.toRideResponseList(rideList)).thenReturn(Collections.emptyList());

        ResponseEntity<RideListResponse> response = rideController.getAllRides();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetRideById() {
        Ride ride = new Ride();
        RideResponse rideResponse = new RideResponse();
        when(rideService.getRide(1L)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> response = rideController.getRideById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
    }

    @Test
    public void testCreateRide() {
        Ride ride = new Ride();
        RideRequest rideRequest = new RideRequest();
        RideResponse rideResponse = new RideResponse();
        when(rideMapper.toModel(rideRequest)).thenReturn(ride);
        when(rideService.createRide(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> response = rideController.createRide(rideRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
    }

    @Test
    public void testChangeRideStatus() {
        Ride ride = new Ride();
        RideResponse rideResponse = new RideResponse();
        when(rideService.changeRideStatus(1L, RideStatus.FINISHED)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        ResponseEntity<RideResponse> response = rideController.changeRideStatus(1L, RideStatus.FINISHED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
    }
}
