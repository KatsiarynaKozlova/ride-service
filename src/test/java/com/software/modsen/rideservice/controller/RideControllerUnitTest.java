package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.service.RideService;
import com.software.modsen.rideservice.util.RideTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.software.modsen.rideservice.util.RideTestUtil.ACCEPTED_RIDE_STATUS;
import static com.software.modsen.rideservice.util.RideTestUtil.DEFAULT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        List<Ride> rideList = List.of(RideTestUtil.getDefaultAcceptedRide());
        when(rideService.gelAllRides()).thenReturn(rideList);

        ResponseEntity<RideListResponse> response = rideController.getAllRides();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(rideService, times(1)).gelAllRides();
    }

    @Test
    public void testGetRideById() {
        Ride ride = RideTestUtil.getDefaultAcceptedRide();
        RideResponse rideResponse = RideTestUtil.getDefaultAcceptedRideResponse();
        when(rideService.getRide(anyLong())).thenReturn(ride);
        when(rideMapper.toResponse(any(Ride.class))).thenReturn(rideResponse);

        ResponseEntity<RideResponse> resultResponse = rideController.getRideById(1L);

        verify(rideService, times(1)).getRide(1L);
        assertEquals(HttpStatus.OK, resultResponse.getStatusCode());
        assertEquals(rideResponse, resultResponse.getBody());
    }

    @Test
    public void testCreateRide() {
        Ride ride = RideTestUtil.getDefaultCreatedRide();
        RideRequest rideRequest = RideTestUtil.getDefaultRideRequest();
        RideResponse expectedRideResponse = RideTestUtil.getDefaultAcceptedRideResponse();

        when(rideMapper.toModel(any(RideRequest.class))).thenReturn(ride);
        when(rideService.createRide(any(Ride.class))).thenReturn(ride);
        when(rideMapper.toResponse(any(Ride.class))).thenReturn(expectedRideResponse);

        ResponseEntity<RideResponse> resultResponse = rideController.createRide(rideRequest);

        assertEquals(HttpStatus.CREATED, resultResponse.getStatusCode());
        assertEquals(expectedRideResponse, resultResponse.getBody());
        verify(rideService, times(1)).createRide(ride);
    }

    @Test
    public void testChangeRideStatus() {
        Ride ride = RideTestUtil.getDefaultCreatedRide();
        RideResponse rideResponse = RideTestUtil.getDefaultAcceptedRideResponse();

        when(rideService.changeRideStatus(anyLong(), any(RideStatus.class))).thenReturn(ride);
        when(rideMapper.toResponse(any(Ride.class))).thenReturn(rideResponse);
        ResponseEntity<RideResponse> response = rideController.changeRideStatus(DEFAULT_ID, ACCEPTED_RIDE_STATUS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rideResponse, response.getBody());
        verify(rideService, times(1)).changeRideStatus(DEFAULT_ID, ACCEPTED_RIDE_STATUS);
    }
}
