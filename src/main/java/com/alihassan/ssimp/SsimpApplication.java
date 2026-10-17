package com.alihassan.ssimp;

import com.alihassan.ssimp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SsimpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsimpApplication.class, args);
    }

    @Bean
    CommandLineRunner checkDatabase(UserRepository userRepository) {
        return args -> {
            long totalUsers = userRepository.count();
            System.out.println("Users in database: " + totalUsers);
        };
    }
}