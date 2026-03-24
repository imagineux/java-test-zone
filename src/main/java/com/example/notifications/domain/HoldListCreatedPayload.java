package com.example.notifications.domain;

import java.time.LocalDate;

public record HoldListCreatedPayload(
        String holdListName,
        String createdBy,
        LocalDate effectiveDate
) implements NotificationPayload {
}
