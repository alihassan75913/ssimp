package com.alihassan.ssimp.controller;

import com.alihassan.ssimp.model.User;
import com.alihassan.ssimp.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("fullName", "");
        model.addAttribute("email", "");
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam(name = "fullName", defaultValue = "") String fullName,
            @RequestParam(name = "email", defaultValue = "") String email,
            @RequestParam(name = "password", defaultValue = "") String password,
            Model model) {

        fullName = fullName.trim();
        email = email.trim().toLowerCase(Locale.ROOT);

        // Keep these fields filled in if there is an error.
        model.addAttribute("fullName", fullName);
        model.addAttribute("email", email);

        String error = null;

        if (fullName.isBlank() || fullName.length() > 100) {
            error = "Enter your full name, using no more than 100 characters.";
        } else if (email.length() > 255
                || !email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            error = "Enter a valid email address.";
        } else if (password.isBlank() || password.length() < 8) {
            error = "Use a password with at least 8 characters.";
        } else if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            error = "Your password is too long. Please use a shorter password.";
        } else if (userRepository.existsByEmail(email)) {
            error = "An account with this email address already exists.";
        }

        if (error != null) {
            model.addAttribute("error", error);
            return "register";
        }

        User account = new User();
        account.setFullName(fullName);
        account.setEmail(email);
        account.setPasswordHash(passwordEncoder.encode(password));
        account.setRole(User.Role.USER);
        account.setActive(true);

        try {
            userRepository.saveAndFlush(account);
        } catch (DataIntegrityViolationException exception) {
            model.addAttribute(
                "error",
                "The account could not be saved. The email may already be registered."
            );
            return "register";
        }

        return "redirect:/register?success";
    }
}