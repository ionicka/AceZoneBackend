package com.shop.AceZone.Controller;

import com.shop.AceZone.Model.CartItem;
import com.shop.AceZone.Model.Product;
import com.shop.AceZone.Model.User;
import com.shop.AceZone.Repository.CartItemRepository;
import com.shop.AceZone.Repository.ProductRepository;
import com.shop.AceZone.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartItemRepository cartRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;

    private User getUser(Authentication auth) {
        return userRepo.findByUsername(auth.getName()).orElseThrow();
    }

    // GET toate items din cart
    @GetMapping
    public List<CartItem> getCart(Authentication auth) {
        return cartRepo.findByUser(getUser(auth));
    }

    // POST adaugă produs în cart
    @PostMapping("/{productId}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId, Authentication auth) {
        User user = getUser(auth);
        Optional<CartItem> existing = cartRepo.findByUserAndProductId(user, productId);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + 1);
            cartRepo.save(item);
            return ResponseEntity.ok(item);
        }

        Product product = productRepo.findById(productId).orElseThrow();
        CartItem item = new CartItem();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(1);
        cartRepo.save(item);
        return ResponseEntity.ok(item);
    }

    // PUT modifică cantitatea
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Long itemId,
                                            @RequestBody java.util.Map<String, Integer> body,
                                            Authentication auth) {
        CartItem item = cartRepo.findById(itemId).orElseThrow();
        if (!item.getUser().getUsername().equals(auth.getName())) {
            return ResponseEntity.status(403).build();
        }
        int qty = body.get("quantity");
        if (qty <= 0) {
            cartRepo.delete(item);
            return ResponseEntity.ok().build();
        }
        item.setQuantity(qty);
        cartRepo.save(item);
        return ResponseEntity.ok(item);
    }

    // DELETE șterge un item
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId, Authentication auth) {
        CartItem item = cartRepo.findById(itemId).orElseThrow();
        if (!item.getUser().getUsername().equals(auth.getName())) {
            return ResponseEntity.status(403).build();
        }
        cartRepo.delete(item);
        return ResponseEntity.ok().build();
    }

    // DELETE golește tot cart-ul
    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication auth) {
        cartRepo.deleteByUser(getUser(auth));
        return ResponseEntity.ok().build();
    }
}