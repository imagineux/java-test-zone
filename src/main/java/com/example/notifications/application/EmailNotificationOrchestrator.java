package com.example.notifications.application;

import com.example.notifications.config.NotificationCatalog;
import com.example.notifications.config.NotificationDefinition;
import com.example.notifications.domain.NotificationPayload;
import com.example.notifications.domain.NotificationType;
import com.example.notifications.model.NotificationModelBuilderRegistry;
import com.example.notifications.publish.EmailPublicationPublisher;
import com.example.notifications.publish.EmailPublicationRequest;
import com.example.notifications.render.TemplateRenderer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailNotificationOrchestrator {

    private final NotificationCatalog catalog;
    private final NotificationModelBuilderRegistry builderRegistry;
    private final TemplateRenderer templateRenderer;
    private final EmailPublicationPublisher publisher;

    public EmailNotificationOrchestrator(
            NotificationCatalog catalog,
            NotificationModelBuilderRegistry builderRegistry,
            TemplateRenderer templateRenderer,
            EmailPublicationPublisher publisher
    ) {
        this.catalog = catalog;
        this.builderRegistry = builderRegistry;
        this.templateRenderer = templateRenderer;
        this.publisher = publisher;
    }

    public void notify(NotificationType type, NotificationPayload payload) {
        NotificationDefinition definition = catalog.getRequired(type);

        if (!definition.isEnabled()) {
            return;
        }

        Map<String, Object> model = builderRegistry.buildModel(type, payload);
        String body = templateRenderer.render(definition.templatePath(), model);

        EmailPublicationRequest request = new EmailPublicationRequest(
                type,
                definition.subject(),
                definition.recipients(),
                body
        );

        publisher.publish(request);
    }
}
