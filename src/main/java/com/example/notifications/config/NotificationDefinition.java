package com.example.notifications.config;

import java.util.List;

public record NotificationDefinition(
        List<String> recipients,
        String templatePath,
        String subject,
        Boolean enabled
) {

    public NotificationDefinition {
        recipients = recipients == null ? List.of() : List.copyOf(recipients);
    }

    public boolean isEnabled() {
        return enabled == null || enabled;
    }
}
