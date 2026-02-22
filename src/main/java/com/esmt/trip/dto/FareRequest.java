package com.esmt.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FareRequest {
    private Long userId;
    private String transportType; // BUS, BRT, TER
    private Integer hourOfDay;    // Pour les heures creuses 10h-16h
    private Integer totalTrips;   // Pour la remise fidélité
}