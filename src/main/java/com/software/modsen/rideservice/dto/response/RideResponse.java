package com.software.modsen.rideservice.dto.response;

import com.software.modsen.rideservice.model.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RideResponse {
    private Long id;
    private Long driverId;
    private Long passengerId;
    private String routeStart;
    private String routeEnd;
    private BigDecimal price;
    private LocalDateTime dateTimeCreate;
    private RideStatus status;
}
