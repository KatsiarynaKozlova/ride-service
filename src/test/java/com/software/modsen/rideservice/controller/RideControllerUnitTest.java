package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
        List<RideResponse> rideResponseList = List.of(RideTestUtil.getDefaultRideResponse());
        RideListResponse expectedResponse = new RideListResponse(rideResponseList);

        when(rideService.getAllRides()).thenReturn(Flux.fromIterable(rideList));
        when(rideMapper.toRideResponseList(rideList)).thenReturn(rideResponseList);

        Mono<ResponseEntity<RideListResponse>> responseRides = rideController.getAllRides();

        StepVerifier.create(responseRides).assertNext(response -> {
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }).verifyComplete();

        verify(rideService, times(1)).getAllRides();
        verify(rideMapper, times(1)).toRideResponseList(rideList);
    }

    @Test
    public void testGetRideById() {
        Ride ride = RideTestUtil.getDefaultAcceptedRide();
        RideResponse rideResponse = RideTestUtil.getDefaultAcceptedRideResponse();
        when(rideService.getRide(anyLong())).thenReturn(Mono.just(ride));
        when(rideMapper.toResponse(any(Ride.class))).thenReturn(rideResponse);

        Mono<ResponseEntity<RideResponse>> resultResponse = rideController.getRideById(1L);

        StepVerifier.create(resultResponse).assertNext(response -> {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(rideResponse, response.getBody());
        }).verifyComplete();

        verify(rideService, times(1)).getRide(1L);
        verify(rideMapper, times(1)).toResponse(ride);
    }

    @Test
    public void testChangeRideStatus() {
        Ride ride = RideTestUtil.getDefaultCreatedRide();
        RideResponse rideResponse = RideTestUtil.getDefaultAcceptedRideResponse();

        when(rideService.changeRideStatus(anyLong(), any(RideStatus.class))).thenReturn(Mono.just(ride));
        when(rideMapper.toResponse(any(Ride.class))).thenReturn(rideResponse);

        Mono<ResponseEntity<RideResponse>> resultResponse = rideController.changeRideStatus(DEFAULT_ID, ACCEPTED_RIDE_STATUS);

        StepVerifier.create(resultResponse).assertNext(response -> {
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(rideResponse, response.getBody());
        }).verifyComplete();

        verify(rideService, times(1)).changeRideStatus(DEFAULT_ID, ACCEPTED_RIDE_STATUS);
    }
}
