package org.example.mediserve;

import lombok.RequiredArgsConstructor;
import org.example.mediserve.domain.entity.UserEntity;
import org.example.mediserve.domain.enums.UserRole;
import org.example.mediserve.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class MediServeApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(MediServeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            UserEntity admin = UserEntity.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .isPaid(true)
                    .role(UserRole.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
