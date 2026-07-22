package com.iexpo.auth.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
