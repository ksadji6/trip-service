package com.esmt.trip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripRequest {
    @NotNull(message = "L'ID utilisateur est requis")
    private Long userId;

    @NotBlank(message = "Le type de transport est requis")
    private String transportType;

    @NotBlank(message = "L'origine est requise")
    private String origin;

    @NotBlank(message = "La destination est requise")
    private String destination;
}