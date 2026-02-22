package com.esmt.trip.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class TripResponse {
    private String tripRef;
    private Long userId;
    private String transportType;
    private String origin;
    private String destination;
    private BigDecimal amountCharged;
    private String status;
    private boolean isFallbackFare;
    private String transactionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}