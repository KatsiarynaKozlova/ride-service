package com.software.modsen.rideservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RideRequest {
    private Long driverId;
    private Long passengerId;
    private String routeStart;
    private String routeEnd;
}
