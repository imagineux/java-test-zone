package com.example.notifications.config;

import com.example.notifications.domain.NotificationType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.Map;

@ConfigurationProperties(prefix = "notifications")
public class NotificationProperties {

    private Map<NotificationType, NotificationDefinition> definitions = new EnumMap<>(NotificationType.class);

    public Map<NotificationType, NotificationDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<NotificationType, NotificationDefinition> definitions) {
        this.definitions = definitions == null ? new EnumMap<>(NotificationType.class) : new EnumMap<>(definitions);
    }
}
