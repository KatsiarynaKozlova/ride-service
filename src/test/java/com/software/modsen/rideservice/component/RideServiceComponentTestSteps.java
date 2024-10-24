package com.software.modsen.rideservice.component;

import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.service.RideService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
public class RideServiceComponentTestSteps {
    @InjectMocks
    private RideService rideService;
    @Mock
    private RideRepository rideRepository;
    private Ride ride = new Ride();

    @Given("Ride request with driverId {long}, passengerId {long}, routeStart {string}, and routeEnd {string}")
    public void rideRequestWithDriverIdPassengerIdRouteStartAndRouteEnd(Long driverId, Long passengerId, String routeStart, String routeEnd) {
        ride.setDriverId(driverId);
        ride.setPassengerId(passengerId);
        ride.setRouteStart(routeStart);
        ride.setRouteEnd(routeEnd);
    }

    @When("Ride is created")
    public void rideIsCreated() {
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride savedRide = invocation.getArgument(0);
            savedRide.setStatus(RideStatus.CREATED);
            savedRide.setCreatedAt(LocalDateTime.now());
            return savedRide;
        });
        ride = rideService.createRide(ride);
    }

    @Then("the ride should have status {string}")
    public void theRideShouldHaveStatus(String status) {
        assertEquals(RideStatus.valueOf(status), ride.getStatus());
    }

    @And("the ride should have driverId {long} and passengerId {long}")
    public void theRideShouldHaveDriverIdAndPassengerId(Long driverId, Long passengerId) {
        assertEquals(driverId, ride.getDriverId());
        assertEquals(passengerId, ride.getPassengerId());
    }

    @Given("an existing ride with id {long} and status {string}")
    public void anExistingRideWithIdAndStatus(Long id, String status) {
        ride = new Ride();
        ride.setId(id);
        ride.setDriverId(1L);
        ride.setPassengerId(2L);
        ride.setRouteStart("Point A");
        ride.setRouteEnd("Point B");
        ride.setStatus(RideStatus.valueOf(status));
        ride.setDateTimeCreate(LocalDateTime.now());
        when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
    }

    @When("the ride status is changed to {string}")
    public void theRideStatusIsChangedTo(String newStatus) {
        when(rideRepository.save(any(Ride.class))).thenAnswer(invocation -> {
            Ride savedRide = invocation.getArgument(0);
            savedRide.setStatus(RideStatus.valueOf(newStatus));
            return savedRide;
        });

        ride = rideService.changeRideStatus(ride.getId(), RideStatus.valueOf(newStatus));
    }

    @Given("an existing ride with id {long}")
    public void anExistingRideWithId(Long id) {
        ride = new Ride();
        ride.setId(id);
        ride.setDriverId(1L);
        ride.setPassengerId(2L);
        ride.setRouteStart("Point A");
        ride.setRouteEnd("Point B");
        ride.setDateTimeCreate(LocalDateTime.now());

        when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
    }

    @When("the id {long} is passed to the findById method")
    public void theIdIsPassedToTheFindByIdMethod(Long id) {
        when(rideRepository.findById(id)).thenReturn(Optional.of(ride));

        ride = rideService.getRide(id);
    }

    @Then("The response should contain ride with id {long}")
    public void theResponseShouldContainRideWithId(Long id) {
        assertEquals(id, ride.getDriverId());
    }
}
