package com.esmt.trip.repository;

import com.esmt.trip.entity.Trip;
import com.esmt.trip.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    // Pour l'historique des trajets (Cas de test TC-TR-03)
    List<Trip> findByUserIdOrderByStartTimeDesc(Long userId);

    // Pour récupérer un trajet spécifique par sa référence unique
    Optional<Trip> findByTripRef(String tripRef);

    /*
     * Calcule le total dépensé par un utilisateur sur une période donnée.
     * Utilisé par le TripService pour envoyer le contexte au Pricing Service.
     */
    @Query("SELECT COALESCE(SUM(t.amountCharged), 0) FROM Trip t " +
            "WHERE t.userId = :userId " +
            "AND t.status = :status " +
            "AND t.startTime >= :since")
    BigDecimal sumAmountByUserIdAndStatusAndStartTimeAfter(
            @Param("userId") Long userId,
            @Param("status") TripStatus status,
            @Param("since") LocalDateTime since);
}