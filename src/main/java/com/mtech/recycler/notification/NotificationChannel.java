package com.mtech.recycler.notification;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.notification.model.NotificationModel;

public interface NotificationChannel {
    boolean send(NotificationModel notificationModel);
}
