package com.shop.AceZone.Repository;

import com.shop.AceZone.Model.Rental;
import com.shop.AceZone.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);
    List<Rental> findByProductId(Long productId);

    // Verifică dacă produsul e deja rezervat în intervalul dat
    @Query("SELECT r FROM Rental r WHERE r.product.id = :productId " +
            "AND r.status != 'CANCELLED' " +
            "AND r.startTime < :endTime AND r.endTime > :startTime")
    List<Rental> findConflictingRentals(@Param("productId") Long productId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);
}