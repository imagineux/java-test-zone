package com.example.notification.model;

public record HoldListCreatedPayload(
        String holdListId,
        String createdBy,
        int itemCount
) {
}
