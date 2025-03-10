package com.software.modsen.rideservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MonthlyRideId implements Serializable {
    private Long driverId;
    private String monthYear;
}
