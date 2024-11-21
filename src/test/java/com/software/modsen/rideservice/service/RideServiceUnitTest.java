package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.util.RideTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.software.modsen.rideservice.util.RideTestUtil.DEFAULT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class RideServiceUnitTest {
    @Mock
    private RideRepository repository;
    @InjectMocks
    private RideService rideService;

    @Test
    void testGetRide() {
        Ride expectedRide = RideTestUtil.getDefaultAcceptedRide();
        when(repository.findById(anyLong())).thenReturn(Mono.just(expectedRide));

        Mono<Ride> resultRide = rideService.getRide(DEFAULT_ID);

        StepVerifier.create(resultRide)
                .expectNext(expectedRide)
                .verifyComplete();

        verify(repository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    void testGetRideNotFound() {
        when(repository.findById(anyLong())).thenReturn(Mono.empty());

        Mono<Ride> result = rideService.getRide(DEFAULT_ID);

        StepVerifier.create(result)
                .expectError(RideNotFoundException.class)
                .verify();

        verify(repository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    void testGetAllRides() {
        List<Ride> expectedRideList = List.of(RideTestUtil.getDefaultAcceptedRide());

        when(repository.findAll()).thenReturn(Flux.fromIterable(expectedRideList));

        Flux<Ride> resultRideList = rideService.getAllRides();

        StepVerifier.create(resultRideList)
                .expectNextSequence(expectedRideList)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllRidesEmpty() {
        when(repository.findAll()).thenReturn(Flux.empty());

        Flux<Ride> resultRideList = rideService.getAllRides();

        StepVerifier.create(resultRideList)
                .expectNextCount(0)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllCreatedRides() {
        List<Ride> expectedRideList = List.of(RideTestUtil.getDefaultCreatedRide());
        when(repository.getRidesByStatusIs(any(RideStatus.class))).thenReturn(Flux.fromIterable(expectedRideList));

        Flux<Ride> resultCreatedRides = rideService.getAllCreatedRides();
        StepVerifier.create(resultCreatedRides)
                .expectNextSequence(expectedRideList)
                .verifyComplete();

        verify(repository, times(1)).getRidesByStatusIs(RideStatus.CREATED);
    }

    @Test
    void testGetAllCreatedRidesEmpty() {
        when(repository.getRidesByStatusIs(any(RideStatus.class))).thenReturn(Flux.empty());

        Flux<Ride> createdRides = rideService.getAllCreatedRides();

        StepVerifier.create(createdRides)
                .expectNextCount(0)
                .verifyComplete();

        verify(repository, times(1)).getRidesByStatusIs(RideStatus.CREATED);
    }

    @Test
    void testChangeRideStatus() {
        Ride ride = RideTestUtil.getDefaultAcceptedRide();

        when(repository.findByIdLocked(anyLong())).thenReturn(Mono.just(ride));
        when(repository.save(any(Ride.class))).thenReturn(Mono.just(ride));

        Mono<Ride> updatedRide = rideService.changeRideStatus(DEFAULT_ID, RideStatus.ACCEPTED);

        StepVerifier.create(updatedRide)
                .expectNext(ride)
                .verifyComplete();

        verify(repository, times(1)).findByIdLocked(DEFAULT_ID);
        verify(repository, times(1)).save(ride);
    }

    @Test
    void testChangeRideStatusNotFound() {
        when(repository.findByIdLocked(anyLong())).thenReturn(Mono.empty());

        Mono<Ride> result = rideService.changeRideStatus(DEFAULT_ID, RideStatus.ACCEPTED);

        StepVerifier.create(result)
                .expectError(RideNotFoundException.class)
                .verify();

        verify(repository, times(1)).findByIdLocked(DEFAULT_ID);
    }
}
