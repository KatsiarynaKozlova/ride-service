package com.software.modsen.rideservice.repository.jpa;

import com.software.modsen.rideservice.model.CanceledRide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanceledRideRepository extends JpaRepository<CanceledRide, Long> {
    @Query(value = "DELETE FROM canceled_rides WHERE id in (SELECT id FROM canceled_rides LIMIT :limit )",
            nativeQuery = true)
    @Modifying
    void deleteTop(Long limit);

    @Query(value = "SELECT * FROM canceled_rides LIMIT :limit",
            nativeQuery = true)
    List<CanceledRide> selectWithLimit(Long limit);
}
