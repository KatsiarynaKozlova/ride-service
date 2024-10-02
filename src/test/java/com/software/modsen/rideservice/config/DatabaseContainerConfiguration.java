package com.software.modsen.rideservice.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;


@TestConfiguration
public class DatabaseContainerConfiguration {
    @Container
    private static final PostgreSQLContainer<?> postgreSQL = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQL::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQL::getUsername);
        registry.add("spring.datasource.password", postgreSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQL::getDriverClassName);
    }

    @BeforeAll
    static void startContainer() {
        postgreSQL.start();
    }

    @AfterAll
    static void stopContainer() {
        postgreSQL.stop();
    }
}
