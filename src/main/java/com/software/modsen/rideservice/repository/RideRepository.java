package com.software.modsen.rideservice.repository;

import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RideRepository extends ReactiveCrudRepository<Ride, Long> {
    Flux<Ride> getRidesByStatusIs(RideStatus status);

    @Query("SELECT * FROM Ride WHERE id = :id FOR UPDATE")
    Mono<Ride> findByIdLocked(Long id);

    @Query("SELECT * FROM rides r WHERE " +
            "(:driverId IS NULL OR r.driver_id = :driverId) AND " +
            "(:passengerId IS NULL OR r.passenger_id = :passengerId) AND " +
            "(:status IS NULL OR r.status = status) AND " +
            "(:startDate IS NULL OR r.created_at >= :startDate) AND " +
            "(:endDate IS NULL OR r.created_at <= :endDate)")
    Flux<Ride> findByFilters(Long driverId, Long passengerId, RideStatus status, LocalDateTime startDate, LocalDateTime endDate);
}
