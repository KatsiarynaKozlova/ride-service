package com.software.modsen.rideservice.repository.jpa;

import com.software.modsen.rideservice.model.MonthlyRide;
import com.software.modsen.rideservice.model.MonthlyRideId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyRideRepository extends JpaRepository<MonthlyRide, MonthlyRideId> {
    @Query(value = "INSERT INTO monthly_rides (driver_id, month_year, trip_count)" +
            " VALUES (:driverId, :monthYear, 1)" +
            "ON CONFLICT (driver_id, month_year) " +
            "DO UPDATE SET trip_count = (monthly_rides.trip_count + 1) " +
            "RETURNING trip_count",
            nativeQuery = true)
    void upsertMonthRide(Long driverId, String monthYear);
}
