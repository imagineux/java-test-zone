package com.example.notification.domain;

public record HoldListRemovedPayload(
        String holdListName,
        String removedBy,
        String reason
) {
}
