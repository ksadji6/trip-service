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
        log.warn("CIRCUIT BREAKER: PricingService indisponible pour {}. Application du tarif de secours.", request.getTransportType());

        // Déterminer le tarif de secours selon le type de transport
        BigDecimal fallbackAmount;
        String type = request.getTransportType().toUpperCase();

        switch (type) {
            case "TER":
                fallbackAmount = new BigDecimal("800");
                break;
            case "BRT":
                fallbackAmount = new BigDecimal("500");
                break;
            case "BUS":
                fallbackAmount = new BigDecimal("200");
                break;
            default:
                fallbackAmount = new BigDecimal("500");
        }

        return FareResponse.builder()
                .finalAmount(fallbackAmount)
                .baseFare(fallbackAmount)
                .appliedDiscounts(Collections.singletonList("Tarif de secours (Mode dégradé)"))
                .isFallback(true)
                .build();
    }
}