package com.example.notification.domain;

public record HoldListCreatedPayload(
        String holdListName,
        String createdBy,
        int itemCount
) {
}
