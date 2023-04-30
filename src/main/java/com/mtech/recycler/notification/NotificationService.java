package com.mtech.recycler.notification;

public abstract class NotificationService {
    protected NotificationChannel notificationChannel;
    public void setNotificationChannel(NotificationChannel channel){
        this.notificationChannel = channel;
    }
    public abstract boolean send(String sendTo, String message);
}
