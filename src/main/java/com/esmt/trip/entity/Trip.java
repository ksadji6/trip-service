package com.esmt.trip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trip_ref", unique = true, nullable = false, updatable = false)
    private String tripRef;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false)
    private TransportType transportType;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(name = "amount_charged", precision = 10, scale = 2)
    private BigDecimal amountCharged;

    @Column(name = "is_fallback_fare")
    private boolean isFallbackFare = false; // Flag crucial pour le test R-01

    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @Column(name = "start_time", updatable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "transaction_id")
    private String transactionId;

    @PrePersist
    public void prePersist() {
        // Génère une référence type TR-XXXXXXXX
        this.tripRef = "TR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.startTime = LocalDateTime.now();
        if (this.status == null) this.status = TripStatus.PENDING;
    }

    // Méthode pour finaliser le trajet avec les données du Billing Service
    public void complete(BigDecimal amount, String txnId, boolean fallback) {
        this.amountCharged = amount;
        this.transactionId = txnId;
        this.isFallbackFare = fallback;
        this.status = TripStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
    }
}