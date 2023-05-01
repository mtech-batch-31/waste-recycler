package com.mtech.recycler.notification;

import com.mtech.recycler.notification.model.NotificationModel;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RequestNotification extends NotificationService {

    public RequestNotification(NotificationChannel channel) {
        super.setNotificationChannel(channel);
    }
    @Override
    public boolean send(NotificationModel notificationModel) {
        notificationModel.setSubject( "Recycle Request notification");
        notificationModel.setMessage(MessageFormat.format("Hi {0}, \n We have received your recycling request and is pending processing. ", notificationModel.user.getFirstName()));
        return super.notificationChannel.send(notificationModel);
    }
}
