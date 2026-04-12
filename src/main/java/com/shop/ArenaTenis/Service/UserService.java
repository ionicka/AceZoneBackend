package com.shop.ArenaTenis.Service;

import com.shop.ArenaTenis.Model.User;
import com.shop.ArenaTenis.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User createAdmin() {
        if (repo.findByUsername("admin").isPresent()) return null;

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin123"));
        admin.setRole("ROLE_ADMIN");

        return repo.save(admin);
    }

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}