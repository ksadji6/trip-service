package com.esmt.trip.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripCompletedEvent {
    private String tripRef;
    private Long userId;
    private BigDecimal amountCharged;
    private String transportType;
    private String origin;
    private String destination;
    private boolean fallback; // Indique si le tarif de secours a été appliqué

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}