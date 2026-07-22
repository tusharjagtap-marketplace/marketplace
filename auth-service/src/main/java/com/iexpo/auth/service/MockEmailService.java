package com.iexpo.auth.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockEmailService implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("================ MOCK EMAIL GATEWAY ================");
        log.info("Sending Email to: {}", to);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
        log.info("==================================================");
    }
}
