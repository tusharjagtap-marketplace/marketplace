package com.iexpo.auth.service;

import com.iexpo.auth.entity.User;
import com.iexpo.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private UserRepository userRepository;

    private final SecureRandom random = new SecureRandom();

    public String generateAndSaveOtp(User user) {
        // Generate a 6-digit OTP
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        // Set OTP and expiration (5 minutes from now)
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        
        userRepository.save(user);

        // In a real application, you would invoke an SMS service here.
        // For testing, we log it to console.
        System.out.println("=================================================");
        System.out.printf("OTP generated for mobile %s: %s (Expires in 5 mins)%n", user.getMobileNumber(), otp);
        System.out.println("=================================================");

        return otp;
    }

    public boolean verifyOtp(User user, String otp) {
        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            return false;
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Clear OTP after successful verification to prevent reuse
        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setMobileVerified(true);
        userRepository.save(user);

        return true;
    }
}
