package com.software.modsen.rideservice.contract;

import com.software.modsen.rideservice.controller.RideController;
import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.service.RideService;
import com.software.modsen.rideservice.util.ExceptionMessages;
import com.software.modsen.rideservice.util.RideTestUtil;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RideBaseContractTest {
    @InjectMocks
    private RideController rideController;
    @Mock
    private RideService rideService;
    @Mock
    private RideMapper rideMapper;

    @BeforeEach
    public void setup() {
        Ride ride = RideTestUtil.getDefaultAcceptedRide();
        when(rideService.getRide(1L)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(RideTestUtil.getDefaultAcceptedRideResponse());
        when(rideService.getRide(99L)).thenThrow(new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, 99L)));
        RestAssuredMockMvc.standaloneSetup(this.rideController);
    }
}
