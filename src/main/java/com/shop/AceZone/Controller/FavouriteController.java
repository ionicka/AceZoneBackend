package com.shop.AceZone.Controller;

import com.shop.AceZone.Model.Favourite;
import com.shop.AceZone.Model.Product;
import com.shop.AceZone.Model.User;
import com.shop.AceZone.Repository.FavouriteRepository;
import com.shop.AceZone.Repository.ProductRepository;
import com.shop.AceZone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    @Autowired private FavouriteRepository favRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;

    private User getUser(Authentication auth) {
        return userRepo.findByUsername(auth.getName()).orElseThrow();
    }

    // GET toate favoritele userului
    @GetMapping
    public List<Favourite> getFavourites(Authentication auth) {
        return favRepo.findByUser(getUser(auth));
    }

    // POST adaugă la favorite
    @PostMapping("/{productId}")
    public ResponseEntity<?> addFavourite(@PathVariable Long productId, Authentication auth) {
        User user = getUser(auth);
        if (favRepo.existsByUserAndProductId(user, productId)) {
            return ResponseEntity.ok().body("Deja în favorite");
        }
        Product product = productRepo.findById(productId).orElseThrow();
        Favourite fav = new Favourite();
        fav.setUser(user);
        fav.setProduct(product);
        favRepo.save(fav);
        return ResponseEntity.ok(fav);
    }

    // DELETE elimină din favorite
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeFavourite(@PathVariable Long productId, Authentication auth) {
        User user = getUser(auth);
        favRepo.deleteByUserAndProductId(user, productId);
        return ResponseEntity.ok().build();
    }
}