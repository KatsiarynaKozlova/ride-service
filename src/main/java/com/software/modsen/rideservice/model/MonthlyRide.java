package com.software.modsen.rideservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "monthly_rides")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "monthly_rides")
@IdClass(MonthlyRideId.class)
public class MonthlyRide {
    @Id
    @Column(name = "driver_id")
    private Long driverId;
    @Id
    @Column(name = "month_year")
    private String monthYear;
    @Column(name = "trip_count")
    private Long tripCount = 1L;
}
