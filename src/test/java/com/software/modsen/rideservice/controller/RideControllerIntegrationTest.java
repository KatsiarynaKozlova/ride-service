package com.software.modsen.rideservice.controller;

import com.software.modsen.rideservice.model.RideStatus;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RideControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testGetByIdRide_ShouldReturnNotFoundException() throws Exception {
        mockMvc.perform(get("/rides/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    public void testCreateRide_ShouldReturnNewRide() throws Exception {
        mockMvc.perform(post("/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"driverId\": 1, \"passengerId\": 2, \"routeStart\": \"A\", \"routeEnd\": \"B\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(RideStatus.CREATED.name()));
    }

    @Test
    @Order(3)
    public void testGetByIdRide_ShouldReturnRide() throws Exception {
        mockMvc.perform(get("/rides/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllRides() throws Exception {
        mockMvc.perform(get("/rides"))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangeRideStatus() throws Exception {
        mockMvc.perform(put("/rides/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"FINISHED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(RideStatus.FINISHED.name()));
    }
}
