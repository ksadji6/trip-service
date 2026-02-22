package com.esmt.trip.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DebitResponse {
    private String txnRef;
    private String status; // SUCCESS, FAILED
    private BigDecimal newBalance;
}