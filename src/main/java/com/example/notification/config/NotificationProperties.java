package com.example.notification.config;

import com.example.notification.model.NotificationType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@ConfigurationProperties(prefix = "notifications")
public class NotificationProperties {

    private Map<NotificationType, NotificationDefinition> email = new EnumMap<>(NotificationType.class);

    public Map<NotificationType, NotificationDefinition> getEmail() {
        return email;
    }

    public void setEmail(Map<NotificationType, NotificationDefinition> email) {
        this.email = email;
    }
}
