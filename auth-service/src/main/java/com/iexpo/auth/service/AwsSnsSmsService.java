package com.iexpo.auth.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AwsSnsSmsService implements SmsService {

    @Value("${aws.sns.accessKey:}")
    private String accessKey;

    @Value("${aws.sns.secretKey:}")
    private String secretKey;

    @Value("${aws.sns.region:ap-south-1}")
    private String region;

    private SnsClient snsClient;

    @PostConstruct
    public void init() {
        log.info("Initializing AWS SNS Client for region: {}", region);
        
        SnsClientBuilder builder = SnsClient.builder()
                .region(Region.of(region));

        if (accessKey != null && !accessKey.trim().isEmpty() &&
            secretKey != null && !secretKey.trim().isEmpty()) {
            log.info("Using static AWS credentials provided in configuration.");
            builder.credentialsProvider(StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
            ));
        } else {
            log.info("AWS credentials not specified. Falling back to DefaultCredentialsProvider (EKS IAM Role/IRSA/Instance Profile).");
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        this.snsClient = builder.build();
    }

    @Override
    public void sendSms(String mobileNumber, String message) {
        try {
            log.info("Publishing SMS via AWS SNS to: {}", mobileNumber);
            
            Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
            smsAttributes.put("AWS.SNS.SMS.SMSType", MessageAttributeValue.builder()
                    .dataType("String")
                    .stringValue("Transactional")
                    .build());

            PublishRequest request = PublishRequest.builder()
                    .message(message)
                    .phoneNumber(mobileNumber)
                    .messageAttributes(smsAttributes)
                    .build();

            PublishResponse result = snsClient.publish(request);
            log.info("SMS sent successfully. MessageId: {}", result.messageId());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", mobileNumber, e.getMessage(), e);
            throw new RuntimeException("SMS sending failed: " + e.getMessage(), e);
        }
    }
}
