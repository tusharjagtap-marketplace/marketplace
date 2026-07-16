package com.iexpo.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "mobile_number", unique = true, nullable = false)
    private String mobileNumber;

    @Column(name = "is_mobile_verified")
    private boolean isMobileVerified;

    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    private String roles; // e.g. "ROLE_USER,ROLE_ADMIN"
}
