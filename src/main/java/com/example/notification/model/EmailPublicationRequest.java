package com.example.notification.model;

import java.util.List;

public record EmailPublicationRequest(
        List<String> recipients,
        String subject,
        String htmlBody
) {
}
