package com.example.notification.config;

import com.example.notification.domain.NotificationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationCatalogTest {

    @Test
    void shouldReturnConfiguredDefinition() {
        NotificationProperties properties = new NotificationProperties();
        NotificationProperties.NotificationDefinition definition = new NotificationProperties.NotificationDefinition();
        definition.setRecipients(List.of("ops@example.com"));
        definition.setSubject("Hold list created");
        definition.setTemplatePath("hold-list-created");
        properties.getEmail().put(NotificationType.HOLD_LIST_CREATED, definition);

        NotificationCatalog catalog = new NotificationCatalog(properties);

        NotificationProperties.NotificationDefinition found = catalog.getRequired(NotificationType.HOLD_LIST_CREATED);

        assertEquals("hold-list-created", found.getTemplatePath());
        assertEquals("Hold list created", found.getSubject());
    }

    @Test
    void shouldThrowWhenTypeIsMissing() {
        NotificationCatalog catalog = new NotificationCatalog(new NotificationProperties());

        assertThrows(IllegalArgumentException.class,
                () -> catalog.getRequired(NotificationType.HOLD_LIST_REMOVED));
    }
}
