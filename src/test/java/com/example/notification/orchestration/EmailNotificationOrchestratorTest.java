package com.example.notification.orchestration;

import com.example.notification.config.NotificationCatalog;
import com.example.notification.config.NotificationDefinition;
import com.example.notification.config.NotificationProperties;
import com.example.notification.domain.HoldListCreatedPayload;
import com.example.notification.domain.HoldListRemovedPayload;
import com.example.notification.model.NotificationType;
import com.example.notification.publish.EmailPublicationPublisher;
import com.example.notification.publish.EmailPublicationRequest;
import com.example.notification.rendering.TemplateRenderer;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotificationOrchestratorTest {

    @Test
    void publishes_using_shared_flow_for_created_notification() {
        NotificationProperties properties = properties(Map.of(
                NotificationType.HOLD_LIST_CREATED,
                new NotificationDefinition(List.of("ops@example.com"), "hold-list-created", "Created", true)
        ));

        AtomicReference<EmailPublicationRequest> published = new AtomicReference<>();
        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                (template, model) -> "rendered:" + template + ":" + model.get("holdListName"),
                published::set,
                List.of(new CreatedBuilder())
        );

        orchestrator.notify(NotificationType.HOLD_LIST_CREATED, new HoldListCreatedPayload("VIP", "alice", 3));

        assertNotNull(published.get());
        assertEquals(NotificationType.HOLD_LIST_CREATED, published.get().notificationType());
        assertEquals("Created", published.get().subject());
        assertEquals("rendered:hold-list-created:VIP", published.get().body());
    }

    @Test
    void skips_publish_when_notification_is_disabled() {
        NotificationProperties properties = properties(Map.of(
                NotificationType.HOLD_LIST_CREATED,
                new NotificationDefinition(List.of("ops@example.com"), "hold-list-created", "Created", false)
        ));

        AtomicReference<EmailPublicationRequest> published = new AtomicReference<>();
        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                (template, model) -> "unused",
                published::set,
                List.of(new CreatedBuilder())
        );

        orchestrator.notify(NotificationType.HOLD_LIST_CREATED, new HoldListCreatedPayload("VIP", "alice", 3));

        assertNull(published.get());
    }

    @Test
    void supports_second_notification_type_without_changing_orchestrator() {
        NotificationProperties properties = properties(Map.of(
                NotificationType.HOLD_LIST_REMOVED,
                new NotificationDefinition(List.of("ops@example.com"), "hold-list-removed", "Removed", true)
        ));

        AtomicReference<EmailPublicationRequest> published = new AtomicReference<>();
        TemplateRenderer renderer = (template, model) -> "rendered:" + template + ":" + model.get("reason");
        EmailPublicationPublisher publisher = published::set;

        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                renderer,
                publisher,
                List.of(new RemovedBuilder())
        );

        orchestrator.notify(NotificationType.HOLD_LIST_REMOVED, new HoldListRemovedPayload("VIP", "bob", "No longer needed"));

        assertNotNull(published.get());
        assertEquals("Removed", published.get().subject());
        assertEquals("rendered:hold-list-removed:No longer needed", published.get().body());
    }

    @Test
    void throws_when_payload_type_does_not_match_builder() {
        NotificationProperties properties = properties(Map.of(
                NotificationType.HOLD_LIST_CREATED,
                new NotificationDefinition(List.of("ops@example.com"), "hold-list-created", "Created", true)
        ));

        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                (template, model) -> "unused",
                request -> { },
                List.of(new CreatedBuilder())
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> orchestrator.notify(NotificationType.HOLD_LIST_CREATED, new HoldListRemovedPayload("VIP", "bob", "x")));

        assertTrue(exception.getMessage().contains("Payload type mismatch"));
    }

    private static NotificationProperties properties(Map<NotificationType, NotificationDefinition> definitions) {
        NotificationProperties properties = new NotificationProperties();
        properties.setEmail(new EnumMap<>(definitions));
        return properties;
    }

    private static class CreatedBuilder implements NotificationModelBuilder<HoldListCreatedPayload> {
        @Override
        public NotificationType type() {
            return NotificationType.HOLD_LIST_CREATED;
        }

        @Override
        public Class<HoldListCreatedPayload> payloadClass() {
            return HoldListCreatedPayload.class;
        }

        @Override
        public Map<String, Object> buildModel(HoldListCreatedPayload payload) {
            return Map.of("holdListName", payload.holdListName());
        }
    }

    private static class RemovedBuilder implements NotificationModelBuilder<HoldListRemovedPayload> {
        @Override
        public NotificationType type() {
            return NotificationType.HOLD_LIST_REMOVED;
        }

        @Override
        public Class<HoldListRemovedPayload> payloadClass() {
            return HoldListRemovedPayload.class;
        }

        @Override
        public Map<String, Object> buildModel(HoldListRemovedPayload payload) {
            return Map.of("reason", payload.reason());
        }
    }
}
