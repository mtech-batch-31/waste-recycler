package com.mtech.recycler.notification;

import org.springframework.stereotype.Component;

@Component
public class RequestNotification extends NotificationService {

    public RequestNotification(NotificationChannel channel) {
        super.setNotificationChannel(channel);
    }
    @Override
    public boolean send(String sendTo, String message) {
        String subject = "Recycle Request notification";
        String body = message;
        String[] sendToList = new String[] {sendTo};
        super.notificationChannel.send(sendToList, body, subject);
        return false;
    }
}
