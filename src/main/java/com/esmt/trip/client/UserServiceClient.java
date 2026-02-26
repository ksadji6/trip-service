package com.esmt.trip.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    // Vérifie l'éligibilité
    @GetMapping("/api/users/passes/validate/{userId}")
    Boolean validatePass(@PathVariable("userId") Long userId);
    @PutMapping("/api/users/{id}/increment-trips")
    void incrementTrips(@PathVariable("id") Long id);
}