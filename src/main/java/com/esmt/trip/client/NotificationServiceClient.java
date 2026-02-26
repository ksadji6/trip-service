package com.esmt.trip.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

    @PostMapping("/api/notifications/trip-confirmation")
    void sendTripConfirmation(
            @RequestParam("passNumber") String passNumber,
            @RequestParam("transport") String transport,
            @RequestParam("departure") String departure,
            @RequestParam("arrival") String arrival,
            @RequestParam("amount") double amount);
}