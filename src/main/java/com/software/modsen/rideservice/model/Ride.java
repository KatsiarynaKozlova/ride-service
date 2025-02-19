package com.software.modsen.rideservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "rides")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "rides")
public class Ride {
    @jakarta.persistence.Id
    @Id
    private Long id;
    @Column(name = "driver_id")
    private Long driverId;
    @Column(name = "passenger_id")
    private Long passengerId;
    @Column(name = "route_start")
    private String routeStart;
    @Column(name = "route_end")
    private String routeEnd;
    private BigDecimal price;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
    @Enumerated(EnumType.STRING)
    private RideStatus status;
}
