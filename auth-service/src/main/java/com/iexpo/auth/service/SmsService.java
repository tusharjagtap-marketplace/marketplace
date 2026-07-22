package com.iexpo.auth.service;

public interface SmsService {
    void sendSms(String mobileNumber, String message);
}
