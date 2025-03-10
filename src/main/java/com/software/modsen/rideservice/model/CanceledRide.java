package com.software.modsen.rideservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "canceled_rides")
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CanceledRide {
    @Id
    private Long id;
    @Column(name = "driver_id")
    private Long driverId;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}
