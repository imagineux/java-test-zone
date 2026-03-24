package com.example.notifications.orchestration;

import com.example.notifications.builder.NotificationModelBuilder;
import com.example.notifications.config.NotificationType;
import com.example.notifications.model.NotificationPayload;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationModelBuilderRegistry {

    private final Map<NotificationType, NotificationModelBuilder<?>> builders;

    public NotificationModelBuilderRegistry(List<NotificationModelBuilder<?>> builders) {
        this.builders = new EnumMap<>(NotificationType.class);
        for (NotificationModelBuilder<?> builder : builders) {
            NotificationModelBuilder<?> existing = this.builders.put(builder.supportsType(), builder);
            if (existing != null) {
                throw new IllegalStateException("Duplicate builder for type: " + builder.supportsType());
            }
        }
    }

    public NotificationModelBuilder<?> getRequired(NotificationType type) {
        NotificationModelBuilder<?> builder = builders.get(type);
        if (builder == null) {
            throw new IllegalArgumentException("No model builder registered for type: " + type);
        }
        return builder;
    }

    @SuppressWarnings("unchecked")
    public <T extends NotificationPayload> NotificationModelBuilder<T> getTyped(NotificationType type, Class<T> payloadType) {
        NotificationModelBuilder<?> rawBuilder = getRequired(type);
        if (!rawBuilder.payloadType().equals(payloadType)) {
            throw new IllegalArgumentException("Payload type mismatch for " + type + ": expected "
                    + rawBuilder.payloadType().getSimpleName() + " but got " + payloadType.getSimpleName());
        }
        return (NotificationModelBuilder<T>) rawBuilder;
    }
}
