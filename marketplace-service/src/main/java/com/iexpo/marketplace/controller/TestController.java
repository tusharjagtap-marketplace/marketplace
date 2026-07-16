package com.iexpo.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/secured")
    public ResponseEntity<Map<String, Object>> getSecuredData(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Hello! You have successfully accessed the secured endpoint in Marketplace Service.");
        response.put("email", jwt.getSubject()); // Subject is typically set to the user's email
        response.put("claims", jwt.getClaims());
        return ResponseEntity.ok(response);
    }
}
