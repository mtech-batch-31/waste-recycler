package com.mtech.recycler.notification;

import com.mtech.recycler.notification.model.NotificationModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EmailVerification extends NotificationService{

    @Value("${com.mtech.recycler.verificationUrl}")
    private String verificationUrl;

    public EmailVerification(NotificationChannel channel){
        super.setNotificationChannel(channel);
    }
    @Override
    public boolean send(NotificationModel notificationModel) {
        notificationModel.setSubject( "Recycling Registration Confirmation");
        notificationModel.setMessage(getVerificationMessage(notificationModel.user.getFirstName()
                                                            , notificationModel.getVerificationToken()));
        return super.notificationChannel.send(notificationModel);
    }

    private String getVerificationMessage(String userName, String token){
        String link = verificationUrl+"?token=" + token;
        link = "http://localhost:3000/registrationConfirm"+"?token=" + token;
        String msg = "<div><p>Hi "+userName+", </p><br>"+
                     "<p>Thank you for registering. Please click on the below link to activate your account:<div></p>\n"+
                     "<p><a href='" + link + "'>Activate Now</a> </p>";
        return msg;
    }
}
