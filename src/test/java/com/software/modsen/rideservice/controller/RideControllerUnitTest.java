package com.software.modsen.rideservice.controller;


import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.service.RideService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

        ResponseEntity<RideListResponse> response = rideController.getAllRides();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(rideService, times(1)).gelAllRides();
    }

    @Test
    public void testGetRideById() {
        Ride ride = new Ride(1L, 1L, 1L, "a","b",null, LocalDateTime.now(), RideStatus.CREATED);
        RideResponse rideResponse = new RideResponse(1L, 1L, 1L, "a","b",null, LocalDateTime.now(), RideStatus.CREATED);
        when(rideService.getRide(anyLong())).thenReturn(ride);

        ResponseEntity<RideResponse> response = rideController.getRideById(1L);

        verify( rideService, times(1)).getRide(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
    }

    @Test
    public void testCreateRide() {
        Ride ride = new Ride(1L, 1L, 1L, "a","b",null, LocalDateTime.now(), RideStatus.CREATED);
        RideRequest rideRequest = new RideRequest(1L, 1L,"a","b");
        RideResponse rideResponse = new RideResponse(1L, 1L, 1L, "a","b",null, LocalDateTime.now(), RideStatus.CREATED);

        when(rideService.createRide(any(Ride.class))).thenReturn(ride);

        ResponseEntity<RideResponse> response = rideController.createRide(rideRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
        verify(rideService, times(1)).createRide(ride);
    }

    @Test
    public void testChangeRideStatus() {
        Ride ride = new Ride();
        RideResponse rideResponse = new RideResponse();
        when(rideService.changeRideStatus(anyLong(), any(RideStatus.class))).thenReturn(ride);

        ResponseEntity<RideResponse> response = rideController.changeRideStatus(1L, RideStatus.FINISHED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
        verify(rideService, times(1)).changeRideStatus(1L, RideStatus.FINISHED);
    }
}
