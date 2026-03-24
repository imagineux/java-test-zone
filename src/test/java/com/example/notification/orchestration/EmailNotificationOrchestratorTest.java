package com.example.notification.orchestration;

import com.example.notification.config.NotificationCatalog;
import com.example.notification.config.NotificationProperties;
import com.example.notification.domain.NotificationType;
import com.example.notification.model.EmailPublicationRequest;
import com.example.notification.model.HoldListCreatedPayload;
import com.example.notification.publish.EmailPublicationPublisher;
import com.example.notification.render.TemplateRenderer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailNotificationOrchestratorTest {

    @Test
    void shouldRenderAndPublishWhenEnabled() {
        NotificationProperties properties = new NotificationProperties();
        NotificationProperties.NotificationDefinition definition = new NotificationProperties.NotificationDefinition();
        definition.setRecipients(List.of("ops@example.com"));
        definition.setSubject("Hold list created");
        definition.setTemplatePath("hold-list-created");
        properties.getEmail().put(NotificationType.HOLD_LIST_CREATED, definition);

        FakeTemplateRenderer renderer = new FakeTemplateRenderer();
        FakePublisher publisher = new FakePublisher();

        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                renderer,
                publisher,
                List.of(new HoldListCreatedModelBuilder())
        );

        HoldListCreatedPayload payload = new HoldListCreatedPayload("HL-123", "alice", 4);
        orchestrator.notify(NotificationType.HOLD_LIST_CREATED, payload);

        assertEquals("hold-list-created", renderer.templatePath);
        assertEquals("HL-123", renderer.model.get("holdListId"));
        assertEquals(1, publisher.published.size());
        assertEquals("Hold list created", publisher.published.getFirst().subject());
    }

    @Test
    void shouldNotPublishWhenDisabled() {
        NotificationProperties properties = new NotificationProperties();
        NotificationProperties.NotificationDefinition definition = new NotificationProperties.NotificationDefinition();
        definition.setRecipients(List.of("ops@example.com"));
        definition.setSubject("Hold list created");
        definition.setTemplatePath("hold-list-created");
        definition.setEnabled(false);
        properties.getEmail().put(NotificationType.HOLD_LIST_CREATED, definition);

        FakePublisher publisher = new FakePublisher();
        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                (templatePath, model) -> "ignored",
                publisher,
                List.of(new HoldListCreatedModelBuilder())
        );

        orchestrator.notify(NotificationType.HOLD_LIST_CREATED, new HoldListCreatedPayload("HL-123", "alice", 4));

        assertEquals(0, publisher.published.size());
    }

    @Test
    void shouldFailFastOnPayloadTypeMismatch() {
        NotificationProperties properties = new NotificationProperties();
        NotificationProperties.NotificationDefinition definition = new NotificationProperties.NotificationDefinition();
        definition.setRecipients(List.of("ops@example.com"));
        definition.setSubject("Hold list created");
        definition.setTemplatePath("hold-list-created");
        properties.getEmail().put(NotificationType.HOLD_LIST_CREATED, definition);

        EmailNotificationOrchestrator orchestrator = new EmailNotificationOrchestrator(
                new NotificationCatalog(properties),
                (templatePath, model) -> "ignored",
                request -> { },
                List.of(new HoldListCreatedModelBuilder())
        );

        assertThrows(IllegalArgumentException.class,
                () -> orchestrator.notify(NotificationType.HOLD_LIST_CREATED, "wrong payload"));
    }

    private static class FakeTemplateRenderer implements TemplateRenderer {
        private String templatePath;
        private Map<String, Object> model;

        @Override
        public String render(String templatePath, Map<String, Object> model) {
            this.templatePath = templatePath;
            this.model = model;
            return "<html>ok</html>";
        }
    }

    private static class FakePublisher implements EmailPublicationPublisher {
        private final List<EmailPublicationRequest> published = new ArrayList<>();

        @Override
        public void publish(EmailPublicationRequest request) {
            published.add(request);
        }
    }
}
