package com.iexpo.auth.controller;

import com.iexpo.auth.dto.*;
import com.iexpo.auth.entity.User;
import com.iexpo.auth.repository.UserRepository;
import com.iexpo.auth.service.JwtService;
import com.iexpo.auth.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email already registered"));
        }
        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Mobile number already registered"));
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .mobileNumber(request.getMobileNumber())
                .isMobileVerified(false)
                .roles("ROLE_USER")
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login/email")
    public ResponseEntity<?> loginWithEmail(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
        }

        User user = userOpt.get();
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .isMobileVerified(user.isMobileVerified())
                .build());
    }

    @PostMapping("/otp/send")
    public ResponseEntity<?> sendOtp(@RequestBody OtpSendRequest request) {
        Optional<User> userOpt = userRepository.findByMobileNumber(request.getMobileNumber());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Mobile number not registered. Please register first."));
        }

        User user = userOpt.get();
        String otp = otpService.generateAndSaveOtp(user);

        // Returning OTP in body and response code for easy verification
        return ResponseEntity.ok(Map.of(
                "message", "OTP generated and sent successfully (logged in console)",
                "otp", otp, // Returning here simplifies testing.
                "mobileNumber", request.getMobileNumber()
        ));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyRequest request) {
        Optional<User> userOpt = userRepository.findByMobileNumber(request.getMobileNumber());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Mobile number not registered"));
        }

        User user = userOpt.get();
        boolean isValid = otpService.verifyOtp(user, request.getOtp());

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid or expired OTP"));
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .isMobileVerified(user.isMobileVerified())
                .build());
    }

    @GetMapping("/jwks")
    public Map<String, Object> jwks() {
        return Map.of("keys", List.of(jwtService.getJwk()));
    }
}
