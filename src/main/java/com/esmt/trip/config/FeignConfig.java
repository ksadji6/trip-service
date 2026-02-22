package com.esmt.trip.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.esmt.trip.client")
public class FeignConfig {
    // Tu peux ajouter ici des Bean de décodage d'erreurs si nécessaire
}