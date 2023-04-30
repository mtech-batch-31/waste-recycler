package com.mtech.recycler.notification;

public interface NotificationChannel {
    boolean send(String[] sendTo, String body, String subject);
}
