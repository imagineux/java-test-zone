package com.example.notifications.application;

import com.example.notifications.builder.HoldListCreatedModelBuilder;
import com.example.notifications.builder.HoldListRemovedModelBuilder;
import com.example.notifications.domain.HoldListCreatedPayload;
import com.example.notifications.domain.NotificationType;
import com.example.notifications.model.NotificationModelBuilderRegistry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotificationModelBuilderRegistryTest {

    @Test
    void delegates_to_correct_builder_for_each_type() {
        NotificationModelBuilderRegistry registry = new NotificationModelBuilderRegistry(
                java.util.List.of(new HoldListCreatedModelBuilder(), new HoldListRemovedModelBuilder())
        );

        Map<String, Object> model = registry.buildModel(
                NotificationType.HOLD_LIST_CREATED,
                new HoldListCreatedPayload("Watchlist-B", "carol", LocalDate.of(2026, 3, 1))
        );

        assertEquals("Watchlist-B", model.get("holdListName"));
        assertEquals("carol", model.get("createdBy"));
    }

    @Test
    void throws_clear_error_for_payload_mismatch() {
        NotificationModelBuilderRegistry registry = new NotificationModelBuilderRegistry(
                java.util.List.of(new HoldListCreatedModelBuilder())
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registry.buildModel(
                        NotificationType.HOLD_LIST_CREATED,
                        new com.example.notifications.domain.HoldListRemovedPayload("x", "y", "z")
                ));

        assertTrue(ex.getMessage().contains("Payload type mismatch"));
    }
}
