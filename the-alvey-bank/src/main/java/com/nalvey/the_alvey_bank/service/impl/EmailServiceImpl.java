package com.nalvey.the_alvey_bank.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nalvey.the_alvey_bank.dto.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender javamailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javamailSender.send(mailMessage);
            System.out.println("Mail sent successfully");

        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
