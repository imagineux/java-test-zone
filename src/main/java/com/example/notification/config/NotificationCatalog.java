package com.example.notification.config;

import com.example.notification.model.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationCatalog {

    private final NotificationProperties properties;

    public NotificationCatalog(NotificationProperties properties) {
        this.properties = properties;
    }

    public NotificationDefinition getRequired(NotificationType type) {
        NotificationDefinition definition = properties.getEmail().get(type);
        if (definition == null) {
            throw new IllegalArgumentException("No notification config found for type: " + type);
        }
        return definition;
    }
}
