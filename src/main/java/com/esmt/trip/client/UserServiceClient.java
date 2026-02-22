package com.esmt.trip.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    // Vérifie l'éligibilité
    @GetMapping("/api/users/passes/validate/{userId}")
    Boolean validatePass(@PathVariable("userId") Long userId);
}