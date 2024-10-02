package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.repository.RideRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
        Long id = 1L;
        Ride mockRide = new Ride();
        mockRide.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(mockRide));

        Ride ride = rideService.getRide(id);

        assertNotNull(ride);
        assertEquals(id, ride.getId());
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetRideNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> {
            rideService.getRide(id);
        });

        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetAllRides() {
        Ride ride1 = new Ride();
        Ride ride2 = new Ride();
        when(repository.findAll()).thenReturn(Arrays.asList(ride1, ride2));

        List<Ride> rides = rideService.gelAllRides();

        assertEquals(2, rides.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllRidesEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Ride> rides = rideService.gelAllRides();

        assertTrue(rides.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllCreatedRides() {
        Ride ride1 = new Ride();
        ride1.setStatus(RideStatus.CREATED);
        when(repository.getRidesByStatusIs(RideStatus.CREATED)).thenReturn(Arrays.asList(ride1));

        List<Ride> createdRides = rideService.getAllCreatedRides();

        assertEquals(1, createdRides.size());
        assertEquals(RideStatus.CREATED, createdRides.get(0).getStatus());
        verify(repository, times(1)).getRidesByStatusIs(RideStatus.CREATED);
    }

    @Test
    void testGetAllCreatedRidesEmpty() {
        when(repository.getRidesByStatusIs(RideStatus.CREATED)).thenReturn(Collections.emptyList());

        List<Ride> createdRides = rideService.getAllCreatedRides();

        assertTrue(createdRides.isEmpty());
        verify(repository, times(1)).getRidesByStatusIs(RideStatus.CREATED);
    }

    @Test
    void testCreateRide() {
        Ride ride = new Ride();
        Ride savedRide = new Ride();
        savedRide.setStatus(RideStatus.CREATED);
        savedRide.setDateTimeCreate(LocalDateTime.now());
        when(repository.save(any(Ride.class))).thenReturn(savedRide);

        Ride resultRide = rideService.createRide(ride);

        assertNotNull(resultRide);
        assertEquals(RideStatus.CREATED, resultRide.getStatus());
        assertNotNull(resultRide.getDateTimeCreate());
        verify(repository, times(1)).save(ride);
    }

    @Test
    void testChangeRideStatus() {
        Long id = 1L;
        Ride ride = new Ride();
        ride.setId(id);
        ride.setStatus(RideStatus.CREATED);
        when(repository.findById(id)).thenReturn(Optional.of(ride));
        when(repository.save(ride)).thenReturn(ride);

        Ride updatedRide = rideService.changeRideStatus(id, RideStatus.ACCEPTED);

        assertNotNull(updatedRide);
        assertEquals(RideStatus.ACCEPTED, updatedRide.getStatus());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(ride);
    }

    @Test
    void testChangeRideStatusNotFound() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> {
            rideService.changeRideStatus(id, RideStatus.ACCEPTED);
        });

        verify(repository, times(1)).findById(id);
    }
}
