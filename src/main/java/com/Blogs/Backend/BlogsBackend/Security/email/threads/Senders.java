package com.Blogs.Backend.BlogsBackend.Security.email.threads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class Senders extends Thread{

    //@Autowired
    private JavaMailSender mailSender;

    String toEmail;
    String subject;
    String body;

    public Senders(String toEmail,
                   String subject,
                   String body,
    JavaMailSender mailSender){

        this.body=body;
        this.subject=subject;
        this.toEmail=toEmail;
        this.mailSender = mailSender;
    }

    public void run(){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("sathwiksuggala2@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}