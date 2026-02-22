package com.esmt.trip.client;

import com.esmt.trip.dto.FareRequest;
import com.esmt.trip.dto.FareResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pricing-service", fallback = PricingServiceClientFallback.class)
public interface PricingServiceClient {
    @PostMapping("/api/pricing/calculate")
    FareResponse calculateFare(@RequestBody FareRequest request);
}