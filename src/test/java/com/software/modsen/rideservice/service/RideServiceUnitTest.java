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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.software.modsen.rideservice.util.RideTestUtil.DEFAULT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedRide));

        Ride resultRide = rideService.getRide(DEFAULT_ID);

        assertNotNull(resultRide);
        assertEquals(DEFAULT_ID, resultRide.getId());
        assertEquals(expectedRide, resultRide);
        verify(repository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    void testGetRideNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.getRide(DEFAULT_ID));

        verify(repository, times(1)).findById(DEFAULT_ID);
    }

    @Test
    void testGetAllRides() {
        List<Ride> expectedRideList = List.of(RideTestUtil.getDefaultAcceptedRide());
        when(repository.findAll()).thenReturn(expectedRideList);

        List<Ride> resultRideList = rideService.gelAllRides();

        assertEquals(1, resultRideList.size());
        assertEquals(expectedRideList, resultRideList);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllRidesEmpty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Ride> resultRideList = rideService.gelAllRides();

        assertTrue(resultRideList.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllCreatedRides() {
        List<Ride> expectedRideList = List.of(RideTestUtil.getDefaultCreatedRide());
        when(repository.getRidesByStatusIs(any(RideStatus.class))).thenReturn(expectedRideList);

        List<Ride> resultCreatedRides = rideService.getAllCreatedRides();

        assertEquals(1, resultCreatedRides.size());
        assertEquals(RideStatus.CREATED, resultCreatedRides.get(0).getStatus());
        assertEquals(expectedRideList, resultCreatedRides);
        verify(repository, times(1)).getRidesByStatusIs(RideStatus.CREATED);
    }

    @Test
    void testGetAllCreatedRidesEmpty() {
        when(repository.getRidesByStatusIs(any(RideStatus.class))).thenReturn(Collections.emptyList());

        List<Ride> createdRides = rideService.getAllCreatedRides();

        assertTrue(createdRides.isEmpty());
        verify(repository, times(1)).getRidesByStatusIs(RideStatus.CREATED);
    }

    @Test
    void testCreateRide() {
        Ride preSavedRide = RideTestUtil.getDefaultPreCreatedRide();
        Ride savedRide = RideTestUtil.getDefaultCreatedRide();

        when(repository.save(any(Ride.class))).thenReturn(savedRide);

        Ride resultRide = rideService.createRide(preSavedRide);

        assertNotNull(resultRide);
        assertEquals(RideStatus.CREATED, resultRide.getStatus());
        assertNotNull(resultRide.getDateTimeCreate());
        verify(repository, times(1)).save(preSavedRide);
    }

    @Test
    void testChangeRideStatus() {
        Ride ride = RideTestUtil.getDefaultAcceptedRide();

        when(repository.findById(anyLong())).thenReturn(Optional.of(ride));
        when(repository.save(any(Ride.class))).thenReturn(ride);

        Ride updatedRide = rideService.changeRideStatus(DEFAULT_ID, RideStatus.ACCEPTED);

        assertNotNull(updatedRide);
        assertEquals(RideStatus.ACCEPTED, updatedRide.getStatus());
        verify(repository, times(1)).findById(DEFAULT_ID);
        verify(repository, times(1)).save(ride);
    }

    @Test
    void testChangeRideStatusNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RideNotFoundException.class, () -> rideService.changeRideStatus(DEFAULT_ID, RideStatus.ACCEPTED));

        verify(repository, times(1)).findById(DEFAULT_ID);
    }
}
