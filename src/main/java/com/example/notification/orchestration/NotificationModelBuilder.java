package com.example.notification.orchestration;

import com.example.notification.domain.NotificationType;

import java.util.Map;

public interface NotificationModelBuilder<T> {

    NotificationType supports();

    Class<T> payloadType();

    Map<String, Object> buildModel(T payload);
}
