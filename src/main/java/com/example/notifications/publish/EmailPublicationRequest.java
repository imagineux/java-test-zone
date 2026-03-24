package com.example.notifications.publish;

import com.example.notifications.domain.NotificationType;

import java.util.List;

public record EmailPublicationRequest(
        NotificationType notificationType,
        String subject,
        List<String> recipients,
        String body
) {
}
