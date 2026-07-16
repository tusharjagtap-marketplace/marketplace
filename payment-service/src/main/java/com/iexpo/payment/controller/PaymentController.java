package com.iexpo.payment.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processPayment(
            @RequestBody PaymentRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("transactionId", UUID.randomUUID().toString());
        response.put("amount", request.getAmount());
        response.put("orderId", request.getOrderId());
        response.put("message", "Payment processed successfully in Payment Service!");
        response.put("paidBy", jwt.getSubject()); // Email of the user who paid
        
        return ResponseEntity.ok(response);
    }

    @Data
    public static class PaymentRequest {
        private double amount;
        private String orderId;
    }
}
