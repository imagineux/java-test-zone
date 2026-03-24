package com.example.notifications.domain;

public record HoldListRemovedPayload(
        String holdListName,
        String removedBy,
        String reason
) implements NotificationPayload {
}
