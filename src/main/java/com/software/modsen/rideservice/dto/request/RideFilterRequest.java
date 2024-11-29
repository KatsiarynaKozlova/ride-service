package com.software.modsen.rideservice.dto.request;

import com.software.modsen.rideservice.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class RideFilterRequest {
    private Long driverId;
    private Long passengerId;
    private RideStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
