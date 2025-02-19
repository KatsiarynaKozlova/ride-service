package com.software.modsen.rideservice.repository.jpa;

import com.software.modsen.rideservice.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideRepositoryBatch extends JpaRepository<Ride, Long> {
    List<Ride> findByFinishedAtAfter(LocalDateTime finishedAt);
}
