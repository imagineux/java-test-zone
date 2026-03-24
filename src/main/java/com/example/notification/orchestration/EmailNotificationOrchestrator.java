package com.example.notification.orchestration;

import com.example.notification.config.NotificationCatalog;
import com.example.notification.config.NotificationDefinition;
import com.example.notification.model.NotificationType;
import com.example.notification.publish.EmailPublicationPublisher;
import com.example.notification.publish.EmailPublicationRequest;
import com.example.notification.rendering.TemplateRenderer;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailNotificationOrchestrator {

    private final NotificationCatalog catalog;
    private final TemplateRenderer renderer;
    private final EmailPublicationPublisher publisher;
    private final Map<NotificationType, NotificationModelBuilder<?>> buildersByType;

    public EmailNotificationOrchestrator(
            NotificationCatalog catalog,
            TemplateRenderer renderer,
            EmailPublicationPublisher publisher,
            List<NotificationModelBuilder<?>> builders
    ) {
        this.catalog = catalog;
        this.renderer = renderer;
        this.publisher = publisher;
        this.buildersByType = new EnumMap<>(NotificationType.class);

        for (NotificationModelBuilder<?> builder : builders) {
            this.buildersByType.put(builder.type(), builder);
        }
    }

    public <T> void notify(NotificationType type, T payload) {
        NotificationDefinition definition = catalog.getRequired(type);
        if (!definition.isEnabled()) {
            return;
        }

        NotificationModelBuilder<T> builder = resolveBuilder(type, payload);
        Map<String, Object> model = builder.buildModel(payload);
        String renderedBody = renderer.render(definition.template(), model);

        EmailPublicationRequest request = new EmailPublicationRequest(
                type,
                definition.recipients(),
                definition.subject(),
                renderedBody
        );
        publisher.publish(request);
    }

    @SuppressWarnings("unchecked")
    private <T> NotificationModelBuilder<T> resolveBuilder(NotificationType type, T payload) {
        NotificationModelBuilder<?> rawBuilder = buildersByType.get(type);
        if (rawBuilder == null) {
            throw new IllegalArgumentException("No NotificationModelBuilder registered for type: " + type);
        }

        if (!rawBuilder.payloadClass().isInstance(payload)) {
            throw new IllegalArgumentException("Payload type mismatch for %s. Expected %s but got %s"
                    .formatted(type, rawBuilder.payloadClass().getSimpleName(), payload.getClass().getSimpleName()));
        }

        return (NotificationModelBuilder<T>) rawBuilder;
    }
}
