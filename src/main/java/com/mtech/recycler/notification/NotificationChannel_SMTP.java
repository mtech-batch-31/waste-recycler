package com.mtech.recycler.notification;

import com.mtech.recycler.notification.model.NotificationModel;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
@Slf4j
@Component("NotificationChannel_SMTP")
public class NotificationChannel_SMTP implements NotificationChannel {
    @Autowired
    private JavaMailSender mailSender;
    private static final String SMTP_FROM_ADDRESS = "RecyclingApp@gmail.com";
    @Override
    public boolean send(NotificationModel notificationModel) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            //SimpleMailMessage msg = new SimpleMailMessage();
            msgHelper.setFrom(SMTP_FROM_ADDRESS);
            msgHelper.setTo(notificationModel.user.getEmail());
            msgHelper.setSubject(notificationModel.getSubject());
            msgHelper.setText(notificationModel.getMessage(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.info("Error at SMTP Notification Channel :"+e.getMessage());
            return false;
        }
        log.info("SMTP Notification Channel : Email sent successfully");
        return true;
    }
}
