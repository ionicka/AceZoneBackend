package com.shop.ArenaTenis.Controller;

import com.shop.ArenaTenis.Dto.LoginRequest;
import com.shop.ArenaTenis.Model.User;
import com.shop.ArenaTenis.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {

        User user = repo.findByUsername(req.getUsername())
                .orElse(null);

        if (user == null) {
            return "User not found";
        }

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return "Wrong password";
        }

        return "Login successful";
    }
}