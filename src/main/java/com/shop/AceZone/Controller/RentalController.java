package com.shop.AceZone.Controller;

import com.shop.AceZone.Model.Product;
import com.shop.AceZone.Model.Rental;
import com.shop.AceZone.Model.User;
import com.shop.AceZone.Repository.ProductRepository;
import com.shop.AceZone.Repository.RentalRepository;
import com.shop.AceZone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired private RentalRepository rentalRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;

    private User getUser(Authentication auth) {
        return userRepo.findByUsername(auth.getName()).orElseThrow();
    }

    // GET toate rezervările userului
    @GetMapping
    public List<Rental> getMyRentals(Authentication auth) {
        return rentalRepo.findByUser(getUser(auth));
    }

    // POST creează o rezervare
    @PostMapping("/{productId}")
    public ResponseEntity<?> createRental(
            @PathVariable Long productId,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        LocalDateTime startTime = LocalDateTime.parse(body.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse(body.get("endTime"));

        if (endTime.isBefore(startTime) || endTime.isEqual(startTime)) {
            return ResponseEntity.badRequest().body("Data de sfârșit trebuie să fie după data de început.");
        }

        // Verifică conflicte
        List<Rental> conflicts = rentalRepo.findConflictingRentals(productId, startTime, endTime);
        if (!conflicts.isEmpty()) {
            return ResponseEntity.badRequest().body("Produsul nu este disponibil în intervalul selectat.");
        }

        Product product = productRepo.findById(productId).orElseThrow();
        User user = getUser(auth);

        // Calculează prețul per oră
        long hours = ChronoUnit.HOURS.between(startTime, endTime);
        if (hours < 1) hours = 1;
        double totalPrice = product.getRentPrice() * hours;

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setProduct(product);
        rental.setStartTime(startTime);
        rental.setEndTime(endTime);
        rental.setTotalPrice(totalPrice);
        rental.setStatus(Rental.RentalStatus.PENDING);

        rentalRepo.save(rental);
        return ResponseEntity.ok(rental);
    }

    // DELETE anulează o rezervare
    @DeleteMapping("/{rentalId}")
    public ResponseEntity<?> cancelRental(@PathVariable Long rentalId, Authentication auth) {
        Rental rental = rentalRepo.findById(rentalId).orElseThrow();
        if (!rental.getUser().getUsername().equals(auth.getName())) {
            return ResponseEntity.status(403).build();
        }
        rental.setStatus(Rental.RentalStatus.CANCELLED);
        rentalRepo.save(rental);
        return ResponseEntity.ok().build();
    }

    // GET verifică disponibilitatea unui produs
    @GetMapping("/{productId}/availability")
    public ResponseEntity<?> checkAvailability(
            @PathVariable Long productId,
            @RequestParam String startTime,
            @RequestParam String endTime) {

        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        List<Rental> conflicts = rentalRepo.findConflictingRentals(productId, start, end);
        return ResponseEntity.ok(Map.of("available", conflicts.isEmpty()));
    }
}