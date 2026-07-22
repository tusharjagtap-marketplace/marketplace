package com.iexpo.auth.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockSmsService implements SmsService {

    @Override
    public void sendSms(String mobileNumber, String message) {
        log.info("================ MOCK SMS GATEWAY ================");
        log.info("Sending SMS to: {}", mobileNumber);
        log.info("Message: {}", message);
        log.info("=================================================");
    }
}
