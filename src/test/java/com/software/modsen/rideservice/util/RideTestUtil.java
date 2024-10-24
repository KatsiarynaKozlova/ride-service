package com.software.modsen.rideservice.util;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

public final class RideTestUtil {
    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_PASSENGER_ID = 1L;
    public static final Long DEFAULT_DRIVER_ID = 1L;
    public static final String DEFAULT_ROUTE_START = "Point A";
    public static final String DEFAULT_ROUTE_END = "Point B";
    public static final BigDecimal DEFAULT_PRICE = new BigDecimal("123.45");
    public static final LocalDateTime DEFAULT_CREATED_AT =
            LocalDateTime.of(2024, Month.SEPTEMBER, 1, 5, 51, 26);
    public static final RideStatus ACCEPTED_RIDE_STATUS = RideStatus.ACCEPTED;
    public static final RideStatus CREATED_RIDE_STATUS = RideStatus.CREATED;

    public static Ride getDefaultCreatedRide() {
        return Ride.builder()
                .id(DEFAULT_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .routeStart(DEFAULT_ROUTE_START)
                .routeEnd(DEFAULT_ROUTE_END)
                .price(DEFAULT_PRICE)
                .dateTimeCreate(DEFAULT_CREATED_AT)
                .status(CREATED_RIDE_STATUS)
                .build();
    }

    public static Ride getDefaultPreCreatedRide() {
        return Ride.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .routeStart(DEFAULT_ROUTE_START)
                .routeEnd(DEFAULT_ROUTE_END)
                .price(DEFAULT_PRICE)
                .dateTimeCreate(DEFAULT_CREATED_AT)
                .status(CREATED_RIDE_STATUS)
                .build();
    }

    public static Ride getDefaultAcceptedRide() {
        return Ride.builder()
                .id(DEFAULT_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .routeStart(DEFAULT_ROUTE_START)
                .routeEnd(DEFAULT_ROUTE_END)
                .price(DEFAULT_PRICE)
                .dateTimeCreate(DEFAULT_CREATED_AT)
                .status(ACCEPTED_RIDE_STATUS)
                .build();
    }

    public static RideResponse getDefaultAcceptedRideResponse() {
        return RideResponse.builder()
                .id(DEFAULT_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .routeStart(DEFAULT_ROUTE_START)
                .routeEnd(DEFAULT_ROUTE_END)
                .price(DEFAULT_PRICE)
                .dateTimeCreate(DEFAULT_CREATED_AT)
                .status(ACCEPTED_RIDE_STATUS)
                .build();
    }

    public static RideResponse getDefaultRideResponse() {
        return RideResponse.builder()
                .id(DEFAULT_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .routeStart(DEFAULT_ROUTE_START)
                .routeEnd(DEFAULT_ROUTE_END)
                .price(DEFAULT_PRICE)
                .dateTimeCreate(DEFAULT_CREATED_AT)
                .status(CREATED_RIDE_STATUS)
                .build();
    }

    public static RideRequest getDefaultRideRequest() {
        return new RideRequest(
                DEFAULT_DRIVER_ID,
                DEFAULT_PASSENGER_ID,
                DEFAULT_ROUTE_START,
                DEFAULT_ROUTE_END
        );
    }
}
