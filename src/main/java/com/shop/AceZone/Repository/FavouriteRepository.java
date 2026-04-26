package com.shop.AceZone.Repository;

import com.shop.AceZone.Model.Favourite;
import com.shop.AceZone.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findByUser(User user);
    Optional<Favourite> findByUserAndProductId(User user, Long productId);
    void deleteByUserAndProductId(User user, Long productId);
    boolean existsByUserAndProductId(User user, Long productId);
}