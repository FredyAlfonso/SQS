package com.sqsfredy.sqs.configuration;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SQSconfig {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;


    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(){
        return new QueueMessagingTemplate(AmazonSQSAsyncClientBuilder());
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory() {
        SimpleMessageListenerContainerFactory msgListenerContainerFactory = new SimpleMessageListenerContainerFactory();
        msgListenerContainerFactory.setAmazonSqs(AmazonSQSAsyncClientBuilder());
        return msgListenerContainerFactory;
    }


    private AmazonSQSAsync AmazonSQSAsyncClientBuilder(){
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                //new BasicAWSCredentials(accessKey,secretKey)
                                new AWSSessionCredentials() {
                                    @Override
                                    public String getSessionToken() {
                                        return  "FwoGZXIvYXdzEG8aDJQIJYl3euS55VDmHyLUAbqZC2NblUTeyn8miC67VP0QFgj35IeZjVCjtAiHBvwRCi75VnI1YjnX2JUiTYijNsNIXKH7Xu05aoOVwbM+YflMnlxVjl40M7nbcEK9/NNN6TPncdFMoXbpSkgvheDrZ0m2xKNRq7Vi5BTiRSCr1OUbcd7Q1+pr4v7dCg9UFSW0D1uwziSIMiriZJxV+OhkZu1IMZt0ils9G8OwRE2x0ux2nUGjp+/6bSW3nN55TPSi6HUGl9JTyQnejQETx8v/RPbDfB24sKUOsf3ywVM5yf8b+jBDKMzDtIwGMi1eD5cjdYlg85ELdKC4I9BKIJslM9wgvDJ2NMWP9K4Wz7AKTsFXTzjsZS8n+8o=";
                                    }

                                    @Override
                                    public String getAWSAccessKeyId() {
                                        return accessKey;
                                    }

                                    @Override
                                    public String getAWSSecretKey() {
                                        return secretKey;
                                    }
                                }
                        )
                )
                .build();
    }
}
