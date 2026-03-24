package com.example.notifications.application;

import com.example.notifications.config.NotificationCatalog;
import com.example.notifications.config.NotificationDefinition;
import com.example.notifications.domain.HoldListCreatedPayload;
import com.example.notifications.domain.HoldListRemovedPayload;
import com.example.notifications.domain.NotificationType;
import com.example.notifications.model.NotificationModelBuilderRegistry;
import com.example.notifications.publish.EmailPublicationPublisher;
import com.example.notifications.publish.EmailPublicationRequest;
import com.example.notifications.render.TemplateRenderer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailNotificationOrchestratorTest {

    private final NotificationCatalog catalog = mock(NotificationCatalog.class);
    private final NotificationModelBuilderRegistry builderRegistry = mock(NotificationModelBuilderRegistry.class);
    private final TemplateRenderer templateRenderer = mock(TemplateRenderer.class);
    private final EmailPublicationPublisher publisher = mock(EmailPublicationPublisher.class);

    private final EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
            catalog, builderRegistry, templateRenderer, publisher
    );

    @Test
    void publishes_email_when_notification_is_enabled() {
        HoldListCreatedPayload payload = new HoldListCreatedPayload("Watchlist-A", "alice", LocalDate.of(2026, 3, 24));

        NotificationDefinition definition = new NotificationDefinition(
                List.of("ops@example.com"),
                "templates/notifications/hold-list-created",
                "Hold list created",
                true
        );

        when(catalog.getRequired(NotificationType.HOLD_LIST_CREATED)).thenReturn(definition);
        when(builderRegistry.buildModel(NotificationType.HOLD_LIST_CREATED, payload)).thenReturn(Map.of("holdListName", "Watchlist-A"));
        when(templateRenderer.render(eq("templates/notifications/hold-list-created"), any())).thenReturn("rendered-body");

        orchestrator.notify(NotificationType.HOLD_LIST_CREATED, payload);

        verify(publisher).publish(new EmailPublicationRequest(
                NotificationType.HOLD_LIST_CREATED,
                "Hold list created",
                List.of("ops@example.com"),
                "rendered-body"
        ));
    }

    @Test
    void skips_publish_when_notification_is_disabled() {
        HoldListRemovedPayload payload = new HoldListRemovedPayload("Watchlist-A", "bob", "cleanup");
        NotificationDefinition definition = new NotificationDefinition(
                List.of("ops@example.com"),
                "templates/notifications/hold-list-removed",
                "Hold list removed",
                false
        );

        when(catalog.getRequired(NotificationType.HOLD_LIST_REMOVED)).thenReturn(definition);

        orchestrator.notify(NotificationType.HOLD_LIST_REMOVED, payload);

        verifyNoInteractions(builderRegistry, templateRenderer, publisher);
    }
}
