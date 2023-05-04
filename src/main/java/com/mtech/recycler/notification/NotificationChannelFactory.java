package com.mtech.recycler.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class NotificationChannelFactory {
    private final BeanFactory beanFactory;

    public enum CHANNEL_TYPE {
        SMTP, SMS
    }
    @Autowired
    public NotificationChannelFactory(BeanFactory beanFactory){
        this.beanFactory = beanFactory;
    }

    public NotificationChannel notificationChannel(CHANNEL_TYPE channelType)
    {
        log.info("NotificationChannel Class: +NotificationChannel_"+channelType);
        return beanFactory.getBean("NotificationChannel_"+channelType, NotificationChannel.class);
    }
}
