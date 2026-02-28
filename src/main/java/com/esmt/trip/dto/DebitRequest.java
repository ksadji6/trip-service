package com.esmt.trip.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebitRequest {
    private Long userId;
    private BigDecimal amount;
    private String tripRef; // Pour la traçabilité
}