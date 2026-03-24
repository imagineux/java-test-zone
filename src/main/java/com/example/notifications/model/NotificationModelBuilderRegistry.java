package com.example.notifications.model;

import com.example.notifications.domain.NotificationPayload;
import com.example.notifications.domain.NotificationType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class NotificationModelBuilderRegistry {

    private final Map<NotificationType, NotificationModelBuilder<?>> buildersByType;

    public NotificationModelBuilderRegistry(List<NotificationModelBuilder<?>> builders) {
        this.buildersByType = new EnumMap<>(NotificationType.class);
        for (NotificationModelBuilder<?> builder : builders) {
            buildersByType.put(builder.type(), builder);
        }
    }

    public NotificationModelBuilder<?> getRequired(NotificationType type) {
        NotificationModelBuilder<?> builder = buildersByType.get(type);
        if (builder == null) {
            throw new IllegalArgumentException("No notification model builder registered for type: " + type);
        }
        return builder;
    }

    public Map<String, Object> buildModel(NotificationType type, NotificationPayload payload) {
        NotificationModelBuilder<?> builder = getRequired(type);
        return buildModelWithCheckedPayload(builder, payload);
    }

    private <T extends NotificationPayload> Map<String, Object> buildModelWithCheckedPayload(
            NotificationModelBuilder<T> builder,
            NotificationPayload payload
    ) {
        if (!builder.payloadClass().isInstance(payload)) {
            throw new IllegalArgumentException(
                    "Payload type mismatch for " + builder.type() + ". Expected "
                            + builder.payloadClass().getSimpleName()
                            + " but got " + payload.getClass().getSimpleName());
        }
        T castedPayload = builder.payloadClass().cast(payload);
        return builder.buildModel(castedPayload);
    }
}
