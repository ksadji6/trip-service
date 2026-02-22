package com.esmt.trip.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data @Builder
public class DebitRequest {
    private Long userId;
    private BigDecimal amount;
    private String tripRef; // Pour la traçabilité
}