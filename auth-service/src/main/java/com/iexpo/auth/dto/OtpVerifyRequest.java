package com.iexpo.auth.dto;

import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String mobileNumber;
    private String otp;
}
