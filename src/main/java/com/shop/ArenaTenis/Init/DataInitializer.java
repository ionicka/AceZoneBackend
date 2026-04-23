package com.shop.ArenaTenis.Init;

import com.shop.ArenaTenis.Model.User;
import com.shop.ArenaTenis.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

    @Component
public class DataInitializer {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Bean
    CommandLineRunner init() {
        return args -> {

            if (repo.findByUsername("admin").isEmpty()) {
                User u = new User();
                u.setUsername("admin");
                u.setPassword(encoder.encode("admin123"));
                u.setRole("ROLE_ADMIN");

                repo.save(u);
            }
        };
    }
}