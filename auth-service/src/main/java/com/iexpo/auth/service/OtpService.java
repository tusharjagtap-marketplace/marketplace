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

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    private final SecureRandom random = new SecureRandom();

    public String generateAndSaveOtp(User user) {
        // Generate a 6-digit OTP
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);

        // Set OTP and expiration (5 minutes from now)
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        
        userRepository.save(user);

        // 1. Send SMS to the user
        try {
            smsService.sendSms(user.getMobileNumber(), "Your marketplace verification OTP is: " + otp + ". Valid for 5 minutes.");
        } catch (Exception e) {
            System.err.println("ERROR: Failed to send SMS OTP via gateway: " + e.getMessage());
        }

        // 2. Send Email to the user
        try {
            String subject = "Marketplace OTP Verification";
            String body = "Hello,\n\nYour Marketplace verification OTP is: " + otp + "\nThis OTP is valid for 5 minutes.\n\nThank you!";
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            System.err.println("ERROR: Failed to send Email OTP: " + e.getMessage());
        }

        System.out.println("=================================================");
        System.out.printf("OTP generated for mobile %s and email %s: %s (Expires in 5 mins)%n", 
                user.getMobileNumber(), user.getEmail(), otp);
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
