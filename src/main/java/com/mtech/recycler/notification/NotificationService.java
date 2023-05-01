package com.mtech.recycler.notification;

import com.mtech.recycler.notification.model.NotificationModel;

public abstract class NotificationService {
    protected NotificationChannel notificationChannel;
    public void setNotificationChannel(NotificationChannel channel){
        this.notificationChannel = channel;
    }
    public abstract boolean send(NotificationModel notificationModel);
}
