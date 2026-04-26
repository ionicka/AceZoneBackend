package com.shop.AceZone.Service;

import com.shop.AceZone.Model.Product;
import com.shop.AceZone.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findByAvailableTrueOrderByIdDesc();
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product update(Long id, Product updated) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        p.setName(updated.getName());
        p.setDescription(updated.getDescription());
        p.setPrice(updated.getPrice());

        p.setCategory(updated.getCategory());
        p.setBrand(updated.getBrand());
        p.setImageUrl(updated.getImageUrl());
        p.setStock(updated.getStock());
        p.setAvailable(updated.getAvailable());
        p.setRentable(updated.getRentable());
        p.setRentPrice(updated.getRentPrice());
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    public Optional<Product> getById(Long id) {
        return repo.findById(id);
    }
}