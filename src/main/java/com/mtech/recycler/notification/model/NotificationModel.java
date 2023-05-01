package com.mtech.recycler.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mtech.recycler.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class NotificationModel {
    public User user;
    String subject;
    String message;

    String verificationToken;
}
