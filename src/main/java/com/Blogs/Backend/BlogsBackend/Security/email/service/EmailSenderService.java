package com.Blogs.Backend.BlogsBackend.Security.email.service;

import com.Blogs.Backend.BlogsBackend.Security.email.threads.Senders;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    ExecutorService executor = Executors.newFixedThreadPool(2);

    public void sendMail(String toEmail,
                         String subject,
                         String body){

        executor.submit(new Senders(toEmail,subject,body,mailSender));

        System.out.println("Mail sent successfully");
    }

    @PreDestroy
    public void shutdownExecutor() {
        executor.shutdown();
    }
}
