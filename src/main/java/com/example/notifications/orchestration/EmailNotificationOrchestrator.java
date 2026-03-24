package com.example.notifications.orchestration;

import com.example.notifications.builder.NotificationModelBuilder;
import com.example.notifications.config.NotificationCatalog;
import com.example.notifications.config.NotificationDefinition;
import com.example.notifications.config.NotificationType;
import com.example.notifications.model.NotificationPayload;
import com.example.notifications.publish.EmailPublicationPublisher;
import com.example.notifications.publish.EmailPublicationRequest;
import com.example.notifications.render.TemplateRenderer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailNotificationOrchestrator {

    private final NotificationCatalog notificationCatalog;
    private final NotificationModelBuilderRegistry registry;
    private final TemplateRenderer templateRenderer;
    private final EmailPublicationPublisher publisher;

    public EmailNotificationOrchestrator(NotificationCatalog notificationCatalog,
                                         NotificationModelBuilderRegistry registry,
                                         TemplateRenderer templateRenderer,
                                         EmailPublicationPublisher publisher) {
        this.notificationCatalog = notificationCatalog;
        this.registry = registry;
        this.templateRenderer = templateRenderer;
        this.publisher = publisher;
    }

    public <T extends NotificationPayload> void publish(NotificationType type, T payload, Class<T> payloadType) {
        NotificationDefinition definition = notificationCatalog.getRequired(type);
        if (!definition.isEnabled()) {
            return;
        }

        NotificationModelBuilder<T> builder = registry.getTyped(type, payloadType);
        Map<String, Object> model = builder.buildModel(payload);
        String body = templateRenderer.render(definition.templatePath(), model);

        EmailPublicationRequest request = new EmailPublicationRequest(
                type,
                definition.recipients(),
                definition.subject(),
                body
        );

        publisher.publish(request);
    }
}
