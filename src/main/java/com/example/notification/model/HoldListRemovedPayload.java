package com.example.notification.model;

public record HoldListRemovedPayload(
        String holdListId,
        String removedBy,
        String reason
) {
}
