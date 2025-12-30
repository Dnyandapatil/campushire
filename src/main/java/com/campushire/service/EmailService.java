package com.campushire.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendStatusUpdateMail(String to, String studentName,
                                     String companyName, String newStatus) {
        String subject = "Application status update - " + companyName;
        String text = "Hi " + studentName + ",\n\n"
                + "Your application status for company " + companyName
                + " has been updated to: " + newStatus + ".\n\n"
                + "Best wishes,\nPlacement Cell";

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}
