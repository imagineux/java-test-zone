package com.example.notifications.model;

public record HoldListRemovedPayload(
        String holdListName,
        String removedBy,
        String removalReason
) implements NotificationPayload {
}
