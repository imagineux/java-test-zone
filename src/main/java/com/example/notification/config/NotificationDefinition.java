package com.example.notification.config;

import java.util.List;

public record NotificationDefinition(
        List<String> recipients,
        String template,
        String subject,
        Boolean enabled
) {
    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
