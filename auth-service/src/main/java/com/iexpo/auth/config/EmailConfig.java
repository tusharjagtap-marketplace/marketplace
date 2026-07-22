package com.iexpo.auth.config;

import com.iexpo.auth.service.AwsSesEmailService;
import com.iexpo.auth.service.EmailService;
import com.iexpo.auth.service.MockEmailService;
import com.iexpo.auth.service.SmtpEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${aws.ses.enabled:false}")
    private boolean sesEnabled;

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Bean
    public EmailService emailService() {
        if (sesEnabled) {
            return new AwsSesEmailService();
        } else if (mailHost != null && !mailHost.trim().isEmpty()) {
            return new SmtpEmailService();
        } else {
            return new MockEmailService();
        }
    }
}
