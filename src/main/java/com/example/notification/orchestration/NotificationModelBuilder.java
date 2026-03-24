package com.example.notification.orchestration;

import com.example.notification.model.NotificationType;

import java.util.Map;

public interface NotificationModelBuilder<T> {

    NotificationType type();

    Class<T> payloadClass();

    Map<String, Object> buildModel(T payload);
}
