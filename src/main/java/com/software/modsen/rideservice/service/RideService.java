package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.exception.RideNotFoundException;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.repository.RideRepository;
import com.software.modsen.rideservice.util.ExceptionMessages;
import com.software.modsen.rideservice.util.LogInfoMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final ReactiveTransactionManager transactionManager;

    public Mono<Ride> getRide(Long id) {
        Mono<Ride> ride = getByIdOrElseThrow(id);
        log.info(String.format(LogInfoMessages.GET_RIDE_BY_ID, id));
        return ride;
    }

    public Flux<Ride> getAllRides() {
        Flux<Ride> rides = rideRepository.findAll();
        log.info(LogInfoMessages.GET_LIST_OF_RIDES);
        return rides;
    }

    public Flux<Ride> getAllCreatedRides() {
        Flux<Ride> rides = rideRepository.getRidesByStatusIs(RideStatus.CREATED);
        log.info(LogInfoMessages.GET_LIST_OF_CREATED_RIDES);
        return rides;
    }

    public Mono<Ride> createRide(Ride ride) {
        ride.setStatus(RideStatus.CREATED);
        ride.setCreatedAt(LocalDateTime.now());
        return rideRepository.save(ride)
                .doOnNext(newRide ->log.info(String.format(LogInfoMessages.CREATE_RIDE_WITH_ID, newRide.getId())));
    }

    public Mono<Ride> acceptRide(Long rideId, Long driverId) {
        Mono<Ride> ride = getByIdOrElseThrow(rideId);
        return ride.flatMap(updetedRide -> {
            updetedRide.setDriverId(driverId);
            return rideRepository.save(updetedRide);
        });
    }

    @Transactional
    public Mono<Ride> changeRideStatus(Long id, RideStatus status) {
        Mono<Ride> ride = getByIdLockedOrELseThrow(id);
        Mono<Ride> updatedRide = ride.flatMap(uRide -> {
            uRide.setStatus(status);
            return rideRepository.save(uRide);
        });
        log.info(String.format(LogInfoMessages.UPDATE_RIDE_STATUS, id));
        return updatedRide;
    }

    private Mono<Ride> getByIdOrElseThrow(Long id) {
        return rideRepository.findById(id)
                .switchIfEmpty(Mono.error(new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, id))));
    }

    private Mono<Ride> getByIdLockedOrELseThrow(Long id) {
        return rideRepository.findByIdLocked(id)
                .switchIfEmpty(Mono.error(new RideNotFoundException(String.format(ExceptionMessages.RIDE_NOT_FOUND_EXCEPTION, id))));
    }
}
