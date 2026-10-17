package com.alihassan.ssimp.service;

import com.alihassan.ssimp.model.User;
import com.alihassan.ssimp.repository.UserRepository;
import java.util.Locale;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        String cleanedEmail = email.trim().toLowerCase(Locale.ROOT);

        User account = userRepository.findByEmail(cleanedEmail)
            .orElseThrow(() ->
                new UsernameNotFoundException("Invalid email or password")
            );

        return org.springframework.security.core.userdetails.User
            .withUsername(account.getEmail())
            .password(account.getPasswordHash())
            .roles(account.getRole().name())
            .disabled(!account.isActive())
            .build();
    }
}