package com.iexpo.auth.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.SesClientBuilder;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
public class AwsSesEmailService implements EmailService {

    @Value("${aws.sns.accessKey:}")
    private String accessKey;

    @Value("${aws.sns.secretKey:}")
    private String secretKey;

    @Value("${aws.sns.region:ap-south-1}")
    private String region;

    @Value("${aws.ses.fromAddress:tusharjagtap1996@gmail.com}")
    private String fromAddress;

    private SesClient sesClient;

    @PostConstruct
    public void init() {
        log.info("Initializing AWS SES Client for region: {}", region);
        SesClientBuilder builder = SesClient.builder()
                .region(Region.of(region));

        if (accessKey != null && !accessKey.trim().isEmpty() &&
            secretKey != null && !secretKey.trim().isEmpty()) {
            log.info("SES: Using static AWS credentials provided in configuration.");
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            ));
        } else {
            log.info("SES: AWS credentials not specified. Falling back to DefaultCredentialsProvider.");
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        this.sesClient = builder.build();
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            log.info("Sending Email via AWS SES from {} to {}", fromAddress, to);
            
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(fromAddress)
                    .destination(Destination.builder().toAddresses(to).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder()
                                    .text(Content.builder().data(body).build())
                                    .build())
                            .build())
                    .build();

            SendEmailResponse response = sesClient.sendEmail(request);
            log.info("Email sent successfully via AWS SES. MessageId: {}", response.messageId());
        } catch (Exception e) {
            log.error("Failed to send Email via AWS SES to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }
}
