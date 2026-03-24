package com.example.notifications.orchestration;

import com.example.notifications.builder.NotificationModelBuilder;
import com.example.notifications.config.NotificationCatalog;
import com.example.notifications.config.NotificationDefinition;
import com.example.notifications.config.NotificationType;
import com.example.notifications.model.HoldListCreatedPayload;
import com.example.notifications.publish.EmailPublicationPublisher;
import com.example.notifications.publish.EmailPublicationRequest;
import com.example.notifications.render.TemplateRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailNotificationOrchestratorTest {

    private NotificationCatalog catalog;
    private NotificationModelBuilderRegistry registry;
    private TemplateRenderer renderer;
    private EmailPublicationPublisher publisher;
    private EmailNotificationOrchestrator orchestrator;

    @BeforeEach
    void setUp() {
        catalog = mock(NotificationCatalog.class);
        registry = mock(NotificationModelBuilderRegistry.class);
        renderer = mock(TemplateRenderer.class);
        publisher = mock(EmailPublicationPublisher.class);
        orchestrator = new EmailNotificationOrchestrator(catalog, registry, renderer, publisher);
    }

    @Test
    void publish_rendersAndPublishes() {
        HoldListCreatedPayload payload = new HoldListCreatedPayload("AML-LOCK", "alice", List.of("A1", "A2"));
        NotificationDefinition definition = new NotificationDefinition(List.of("ops@example.com"), "hold-list-created", "Hold list created", true);

        @SuppressWarnings("unchecked")
        NotificationModelBuilder<HoldListCreatedPayload> builder = mock(NotificationModelBuilder.class);

        when(catalog.getRequired(NotificationType.HOLD_LIST_CREATED)).thenReturn(definition);
        when(registry.getTyped(NotificationType.HOLD_LIST_CREATED, HoldListCreatedPayload.class)).thenReturn(builder);
        when(builder.buildModel(payload)).thenReturn(Map.of("holdListName", "AML-LOCK"));
        when(renderer.render("hold-list-created", Map.of("holdListName", "AML-LOCK"))).thenReturn("rendered-body");

        orchestrator.publish(NotificationType.HOLD_LIST_CREATED, payload, HoldListCreatedPayload.class);

        verify(publisher).publish(new EmailPublicationRequest(
                NotificationType.HOLD_LIST_CREATED,
                List.of("ops@example.com"),
                "Hold list created",
                "rendered-body"
        ));
    }

    @Test
    void publish_skipsWhenDisabled() {
        HoldListCreatedPayload payload = new HoldListCreatedPayload("AML-LOCK", "alice", List.of("A1"));
        NotificationDefinition definition = new NotificationDefinition(List.of("ops@example.com"), "hold-list-created", "Hold list created", false);

        when(catalog.getRequired(NotificationType.HOLD_LIST_CREATED)).thenReturn(definition);

        orchestrator.publish(NotificationType.HOLD_LIST_CREATED, payload, HoldListCreatedPayload.class);

        verifyNoInteractions(registry, renderer, publisher);
    }
}
