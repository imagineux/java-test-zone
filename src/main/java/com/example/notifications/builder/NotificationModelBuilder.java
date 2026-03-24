package com.example.notifications.builder;

import com.example.notifications.config.NotificationType;
import com.example.notifications.model.NotificationPayload;

import java.util.Map;

public interface NotificationModelBuilder<T extends NotificationPayload> {

    NotificationType supportsType();

    Class<T> payloadType();

    Map<String, Object> buildModel(T payload);
}
