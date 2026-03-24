package com.example.notifications.model;

import java.util.List;

public record HoldListCreatedPayload(
        String holdListName,
        String createdBy,
        List<String> impactedAccounts
) implements NotificationPayload {
}
