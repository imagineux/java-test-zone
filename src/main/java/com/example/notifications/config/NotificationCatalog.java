package com.example.notifications.config;

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
            throw new IllegalArgumentException("Missing notification configuration for type: " + type);
        }
        return definition;
    }
}
