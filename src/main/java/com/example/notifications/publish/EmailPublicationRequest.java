package com.example.notifications.publish;

import com.example.notifications.config.NotificationType;

import java.util.List;

public record EmailPublicationRequest(
        NotificationType notificationType,
        List<String> recipients,
        String subject,
        String body
) {
}
