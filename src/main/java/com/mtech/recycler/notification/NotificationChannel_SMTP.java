package com.mtech.recycler.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
@Slf4j
@Component("NotificationChannel_SMTP")
public class NotificationChannel_SMTP implements NotificationChannel {
    @Autowired
    private JavaMailSender mailSender;
    private static final String SMTP_FROM_ADDRESS = "RecyclingApp@gmail.com";
    @Override
    public boolean send(String[] toEmails, String body, String subject) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(SMTP_FROM_ADDRESS);
            msg.setTo(toEmails);
            msg.setText(body);
            msg.setSubject(subject);
            mailSender.send(msg);
        } catch (Exception e) {
            log.info("Error at SMTP Notification Channel :"+e.getMessage());
            return false;
        }
        log.info("SMTP Notification Channel : Email sent successfully");
        return true;
    }
}
