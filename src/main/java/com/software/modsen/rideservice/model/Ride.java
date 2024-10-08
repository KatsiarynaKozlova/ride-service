package com.software.modsen.rideservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id")
    private Long id;
    @Column(name = "driver_id")
    private Long driverId;
    @Column(name = "passenger_id")
    private Long passengerId;
    @Column(name = "route_start")
    private String routeStart;
    @Column(name = "route_end")
    private String routeEnd;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "date_time_create")
    private LocalDateTime dateTimeCreate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RideStatus status;
}
