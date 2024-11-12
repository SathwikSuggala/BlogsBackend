package com.Blogs.Backend.BlogsBackend.Security.SMS.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);  // Configurable thread pool size

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSMS(String to, String otp) {
        executorService.submit(() -> {
            try {
                Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(fromNumber),
                        otp
                ).create();
            } catch (Exception e) {
                // Handle exception, e.g., log error
                System.err.println("Failed to send SMS: " + e.getMessage());
            }
        });
    }

    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
    }
}
