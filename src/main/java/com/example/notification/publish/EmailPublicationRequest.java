package com.example.notification.publish;

import com.example.notification.model.NotificationType;

import java.util.List;

public record EmailPublicationRequest(
        NotificationType notificationType,
        List<String> recipients,
        String subject,
        String body
) {
}
