package com.example.notifications.config;

import com.example.notifications.domain.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationCatalog {

    private final NotificationProperties properties;

    public NotificationCatalog(NotificationProperties properties) {
        this.properties = properties;
    }

    public NotificationDefinition getRequired(NotificationType type) {
        NotificationDefinition definition = properties.getDefinitions().get(type);
        if (definition == null) {
            throw new IllegalArgumentException("No notification definition configured for type: " + type);
        }
        return definition;
    }
}
