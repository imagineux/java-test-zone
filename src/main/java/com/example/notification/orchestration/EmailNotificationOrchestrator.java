package com.example.notification.orchestration;

import com.example.notification.config.NotificationCatalog;
import com.example.notification.config.NotificationProperties;
import com.example.notification.domain.NotificationType;
import com.example.notification.model.EmailPublicationRequest;
import com.example.notification.publish.EmailPublicationPublisher;
import com.example.notification.render.TemplateRenderer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailNotificationOrchestrator {

    private final NotificationCatalog catalog;
    private final TemplateRenderer templateRenderer;
    private final EmailPublicationPublisher publisher;
    private final Map<NotificationType, NotificationModelBuilder<?>> builders;

    public EmailNotificationOrchestrator(NotificationCatalog catalog,
                                         TemplateRenderer templateRenderer,
                                         EmailPublicationPublisher publisher,
                                         List<NotificationModelBuilder<?>> modelBuilders) {
        this.catalog = catalog;
        this.templateRenderer = templateRenderer;
        this.publisher = publisher;
        this.builders = indexByType(modelBuilders);
    }

    public <T> void notify(NotificationType type, T payload) {
        NotificationProperties.NotificationDefinition definition = catalog.getRequired(type);
        if (!definition.isEnabled()) {
            return;
        }

        NotificationModelBuilder<T> builder = getBuilder(type, payload);
        Map<String, Object> model = builder.buildModel(payload);
        String htmlBody = templateRenderer.render(definition.getTemplatePath(), model);

        EmailPublicationRequest request = new EmailPublicationRequest(
                definition.getRecipients(),
                definition.getSubject(),
                htmlBody
        );
        publisher.publish(request);
    }

    private Map<NotificationType, NotificationModelBuilder<?>> indexByType(List<NotificationModelBuilder<?>> modelBuilders) {
        Map<NotificationType, NotificationModelBuilder<?>> indexed = new HashMap<>();
        for (NotificationModelBuilder<?> builder : modelBuilders) {
            NotificationModelBuilder<?> previous = indexed.put(builder.supports(), builder);
            if (previous != null) {
                throw new IllegalStateException("Duplicate builder for type: " + builder.supports());
            }
        }
        return indexed;
    }

    @SuppressWarnings("unchecked")
    private <T> NotificationModelBuilder<T> getBuilder(NotificationType type, T payload) {
        NotificationModelBuilder<?> candidate = builders.get(type);
        if (candidate == null) {
            throw new IllegalArgumentException("No model builder found for type: " + type);
        }
        if (!candidate.payloadType().isInstance(payload)) {
            throw new IllegalArgumentException(
                    "Payload type mismatch for " + type + ". Expected "
                            + candidate.payloadType().getSimpleName() + " but got " + payload.getClass().getSimpleName()
            );
        }
        return (NotificationModelBuilder<T>) candidate;
    }
}
