package com.example.notifications.model;

import com.example.notifications.domain.NotificationPayload;
import com.example.notifications.domain.NotificationType;

import java.util.Map;

public interface NotificationModelBuilder<T extends NotificationPayload> {

    NotificationType type();

    Class<T> payloadClass();

    Map<String, Object> buildModel(T payload);
}
