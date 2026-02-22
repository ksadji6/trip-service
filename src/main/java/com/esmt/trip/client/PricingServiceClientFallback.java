package com.esmt.trip.client;

import com.esmt.trip.dto.FareRequest;
import com.esmt.trip.dto.FareResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Collections;

@Component
@Slf4j
public class PricingServiceClientFallback implements PricingServiceClient {

    @Override
    public FareResponse calculateFare(FareRequest request) {
        log.warn("CIRCUIT BREAKER: PricingService indisponible. Application du tarif de secours[cite: 28, 180].");

        // Tarif de secours (fallback) fixé à 500 XOF selon les specs
        BigDecimal fallbackAmount = new BigDecimal("500");

        return FareResponse.builder()
                .finalAmount(fallbackAmount)
                .baseFare(fallbackAmount)
                .appliedDiscounts(Collections.emptyList())
                .isFallback(true) // Marqueur pour la base de données (isFallbackFare=true)
                .build();
    }
}