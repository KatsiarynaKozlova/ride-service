package com.software.modsen.rideservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.modsen.rideservice.dto.request.RideRequest;
import com.software.modsen.rideservice.dto.response.RideResponse;
import com.software.modsen.rideservice.model.RideStatus;
import com.software.modsen.rideservice.util.RideTestUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.software.modsen.rideservice.util.RideTestUtil.ACCEPTED_RIDE_STATUS;
import static com.software.modsen.rideservice.util.RideTestUtil.DEFAULT_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RideControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testGetByIdRide_ShouldReturnNotFoundException() throws Exception {
        mockMvc.perform(get("/rides/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    public void testCreateRide_ShouldReturnNewRide() throws Exception {
        RideRequest newRideRequest = RideTestUtil.getDefaultRideRequest();
        mockMvc.perform(post("/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRideRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(RideStatus.CREATED.name()));
    }

    @Test
    @Order(3)
    public void testGetByIdRide_ShouldReturnRide() throws Exception {
        RideResponse expectedRideResponse = RideTestUtil.getDefaultRideResponse();
        mockMvc.perform(get("/rides/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedRideResponse.getId()));
    }

    @Test
    public void testGetAllRides() throws Exception {
        mockMvc.perform(get("/rides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    public void testChangeRideStatus() throws Exception {
        mockMvc.perform(put("/rides/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"ACCEPTED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ACCEPTED_RIDE_STATUS.name()))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID));
    }
}
