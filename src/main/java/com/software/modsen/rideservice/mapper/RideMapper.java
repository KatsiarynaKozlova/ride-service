package com.software.modsen.rideservice.mapper;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.model.Ride;
import org.mapstruct.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RideMapper {
    RideResponse toResponse(Ride ride);
    Ride toModel(RideRequest rideRequest);
    List<RideResponse> toRideResponseList(List<Ride> rideList);
}
