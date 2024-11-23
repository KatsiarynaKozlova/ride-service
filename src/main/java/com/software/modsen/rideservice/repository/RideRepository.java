package com.software.modsen.rideservice.repository;

import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RideRepository extends ReactiveCrudRepository<Ride, Long> {
    Flux<Ride> getRidesByStatusIs(RideStatus status);

    @Query("SELECT * FROM Ride WHERE id = :id FOR UPDATE")
    Mono<Ride> findByIdLocked(Long id);
}
