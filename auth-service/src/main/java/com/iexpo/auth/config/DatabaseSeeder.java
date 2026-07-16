package com.iexpo.auth.config;

import com.iexpo.auth.entity.User;
import com.iexpo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String testEmail = "tusharjagtap1996@gmail.com";
        String testMobile = "8975526172";
        String testPassword = "tushar@2026";

        if (userRepository.findByEmail(testEmail).isEmpty()) {
            User testUser = User.builder()
                    .email(testEmail)
                    .password(passwordEncoder.encode(testPassword))
                    .mobileNumber(testMobile)
                    .isMobileVerified(false) // Initially false, verified via OTP
                    .roles("ROLE_USER")
                    .build();

            userRepository.save(testUser);
            System.out.println("=================================================");
            System.out.println("DatabaseSeeder: Test user created successfully!");
            System.out.println("Email: " + testEmail);
            System.out.println("Password: " + testPassword);
            System.out.println("Mobile Number: " + testMobile);
            System.out.println("=================================================");
        } else {
            System.out.println("DatabaseSeeder: Test user already exists.");
        }
    }
}
