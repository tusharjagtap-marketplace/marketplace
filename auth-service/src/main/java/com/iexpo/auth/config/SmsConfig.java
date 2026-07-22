package com.iexpo.auth.config;

import com.iexpo.auth.service.AwsSnsSmsService;
import com.iexpo.auth.service.MockSmsService;
import com.iexpo.auth.service.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Bean
    @ConditionalOnProperty(name = "aws.sns.enabled", havingValue = "true")
    public SmsService awsSnsSmsService() {
        return new AwsSnsSmsService();
    }

    @Bean
    @ConditionalOnProperty(name = "aws.sns.enabled", havingValue = "false", matchIfMissing = true)
    public SmsService mockSmsService() {
        return new MockSmsService();
    }
}
