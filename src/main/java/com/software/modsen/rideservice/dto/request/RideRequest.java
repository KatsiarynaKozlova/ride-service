package com.software.modsen.rideservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RideRequest {
    private String routeStart;
    private String routeEnd;
}
