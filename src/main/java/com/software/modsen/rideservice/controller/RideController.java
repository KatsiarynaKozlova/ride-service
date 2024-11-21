package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideListResponse;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.mapper.RideMapper;
import com.software.modsen.rideservice.model.Ride;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.security.User;
import com.software.modsen.rideservice.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
@Tag(name = "Rides")
public class RideController {
    private final RideService rideService;
    private final RideMapper rideMapper;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(description = "Get list of all existing rides ")
    @ApiResponse(responseCode = "200", description = "List of all Rides",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RideListResponse.class))})
    public Mono<ResponseEntity<RideListResponse>> getAllRides() {
        return rideService.getAllRides()
                .collectList()
                .map(rideList -> {
                    List<RideResponse> rideResponseList = rideMapper.toRideResponseList(rideList);
                    return ResponseEntity.ok(new RideListResponse(rideResponseList));
                });
    }

    @GetMapping("/{id}")
    @Operation(description = "Get ride by ID ",
            parameters = {@Parameter(name = "id", description = "This is the ride ID that will be searched for")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Ride by ID",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RideResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(hidden = true)))}
    )
    public Mono<ResponseEntity<RideResponse>> getRideById(@PathVariable Long id) {
        return rideService.getRide(id)
                .map(ride -> ResponseEntity.ok(rideMapper.toResponse(ride)));
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER','ROLE_ADMIN')")
    @GetMapping("/free")
    @Operation(description = "Get list of all free rides(without driver) ")
    @ApiResponse(responseCode = "200", description = "List of all Rides without drivers",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RideListResponse.class))})
    public Mono<ResponseEntity<RideListResponse>> gelAllFreeRides() {
        return rideService.getAllCreatedRides()
                .collectList()
                .map(rideList -> {
                    List<RideResponse> rideResponseList = rideMapper.toRideResponseList(rideList);
                    return ResponseEntity.ok(new RideListResponse(rideResponseList));
                });
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER','ROLE_ADMIN')")
    @PostMapping("/accept/{id}")
    public Mono<ResponseEntity<RideResponse>> acceptRide(@PathVariable Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return rideService.acceptRide(id, user.getId())
                .map(acceptedRide -> ResponseEntity.ok().body(rideMapper.toResponse(acceptedRide)));
    }

    @PreAuthorize("hasAnyRole('ROLE_PASSENGER','ROLE_ADMIN')")
    @PostMapping
    @Operation(description = "Create new ride with status Created ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RideRequest.class))))
    @ApiResponse(responseCode = "200", description = "Create Ride",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = RideResponse.class))})
    public Mono<ResponseEntity<RideResponse>> createRide(@RequestBody RideRequest rideRequest) {
        Ride ride = rideMapper.toModel(rideRequest);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ride.setPassengerId(user.getId());
        return rideService.createRide(ride)
                .map(savedRide -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(rideMapper.toResponse(savedRide)));
    }

    @PutMapping("/{id}/status")
    @Operation(description = "Update Ride status ",
            parameters = {@Parameter(name = "id", description = "This is the ride ID that will be updated")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RideStatus.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Ride",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RideResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Ride not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public Mono<ResponseEntity<RideResponse>> changeRideStatus(@PathVariable Long id, @RequestBody RideStatus status) {
        return rideService.changeRideStatus(id, status)
                .map(updatedRide -> ResponseEntity.ok(rideMapper.toResponse(updatedRide)));
    }
}
