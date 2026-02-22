package com.esmt.trip.controller;

import com.esmt.trip.dto.TripRequest;
import com.esmt.trip.dto.TripResponse;
import com.esmt.trip.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Tag(name = "Trip Service", description = "Gestion des trajets et de la mobilité")
public class TripController {

    private final TripService tripService;

    // enregistrer un trajet.
    @PostMapping("/register")
    @Operation(summary = "Enregistrer un nouveau trajet (BRT, TER, BUS)")
    public ResponseEntity<TripResponse> registerTrip(@Valid @RequestBody TripRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.registerTrip(request));
    }

    //consulter l'historique d'un utilisateur.

    @GetMapping("/history/{userId}")
    @Operation(summary = "Obtenir l'historique des trajets d'un utilisateur")
    public ResponseEntity<List<TripResponse>> getTripHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(tripService.getTripHistory(userId));
    }
}