package com.esmt.trip.service;

import com.esmt.trip.client.*;
import com.esmt.trip.dto.*;
import com.esmt.trip.entity.*;
import com.esmt.trip.event.TripCompletedEvent;
import com.esmt.trip.repository.TripRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final UserServiceClient userServiceClient;
    private final PricingServiceClient pricingServiceClient;
    private final BillingServiceClient billingServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.rabbitmq.exchange:smart-mobility.exchange}")
    private String exchange;

    @Transactional
    public TripResponse registerTrip(TripRequest request) {
        log.info("Enregistrement trajet pour userId={}, type={}", request.getUserId(), request.getTransportType());

        // 1. Validation du Pass (Appel Feign vers User-Service)
        boolean passValid = userServiceClient.validatePass(request.getUserId());
        if (!passValid) {
            throw new RuntimeException("Mobility Pass invalide ou suspendu pour userId: " + request.getUserId());
        }

        // 2. Initialisation du trajet (Statut PENDING)
        Trip trip = Trip.builder()
                .userId(request.getUserId())
                .transportType(TransportType.valueOf(request.getTransportType().toUpperCase()))
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .status(TripStatus.PENDING)
                .build();

        // La tripRef est générée par @PrePersist dans ton entité
        trip = tripRepository.save(trip);

        // 3. Calcul du tarif (Pricing-Service)
        FareRequest fareRequest = FareRequest.builder()
                .userId(request.getUserId())
                .transportType(request.getTransportType())
                .hourOfDay(LocalDateTime.now().getHour())
                .build();

        FareResponse fareResponse = pricingServiceClient.calculateFare(fareRequest);
        log.info("Tarif calculé: {} XOF (fallback={})", fareResponse.getFinalAmount(), fareResponse.isFallback());

        // 4. Débit du compte (Appel Feign vers Billing-Service)
        DebitRequest debitRequest = DebitRequest.builder()
                .userId(request.getUserId())
                .amount(fareResponse.getFinalAmount())
                .tripRef(trip.getTripRef())
                .build();
        DebitResponse debitResponse = billingServiceClient.debit(debitRequest);

        // 5. Finalisation du trajet en base de données
        trip.complete(fareResponse.getFinalAmount(), debitResponse.getTxnRef(), fareResponse.isFallback());
        trip = tripRepository.save(trip);

        // --- 6. NOTIFICATION SYNCHRONE (FEIGN) ---
        // On l'ajoute ici pour une confirmation immédiate à l'utilisateur
        try {
            log.info("Envoi confirmation synchrone via Feign...");
            notificationServiceClient.sendTripConfirmation(
                    "PASS-" + trip.getUserId(), // Génération du numéro de pass
                    trip.getTransportType().name(),
                    trip.getOrigin(),
                    trip.getDestination(),
                    trip.getAmountCharged().doubleValue()
            );
        } catch (Exception e) {
            log.error("Échec notification Feign (Service indisponible) : {}", e.getMessage());
            // On ne bloque pas le trajet si la notification échoue
        }

        // --- 7. NOTIFICATION ASYNCHRONE (RABBITMQ MANUEL) ---
        // Conversion manuelle en JSON pour éviter l'erreur "deprecated since version 4"
        try {
            TripCompletedEvent event = TripCompletedEvent.builder()
                    .tripRef(trip.getTripRef())
                    .userId(request.getUserId())
                    .amountCharged(fareResponse.getFinalAmount())
                    .transportType(request.getTransportType())
                    .origin(request.getOrigin())
                    .destination(request.getDestination())
                    .fallback(fareResponse.isFallback())
                    .build();

            String jsonEvent = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(exchange, "trip.completed.event", jsonEvent);
            log.info("Événement RabbitMQ envoyé avec succès.");
        } catch (Exception e) {
            log.error("Échec envoi RabbitMQ : {}", e.getMessage());
        }

        return toTripResponse(trip);
    }
    public List<TripResponse> getTripHistory(Long userId) {
        return tripRepository.findByUserIdOrderByStartTimeDesc(userId)
                .stream().map(this::toTripResponse).collect(Collectors.toList());
    }

    private TripResponse toTripResponse(Trip trip) {
        return TripResponse.builder()
                .tripRef(trip.getTripRef())
                .userId(trip.getUserId())
                .transportType(trip.getTransportType().name())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .amountCharged(trip.getAmountCharged())
                .status(trip.getStatus().name())
                .isFallbackFare(trip.isFallbackFare())
                .transactionId(trip.getTransactionId())
                .startTime(trip.getStartTime())
                .endTime(trip.getEndTime())
                .build();
    }
}