package com.example.notifications.orchestration;

import com.example.notifications.builder.NotificationModelBuilder;
import com.example.notifications.config.NotificationType;
import com.example.notifications.model.HoldListCreatedPayload;
import com.example.notifications.model.HoldListRemovedPayload;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotificationModelBuilderRegistryTest {

    @Test
    void getTyped_returnsBuilderWhenTypeMatches() {
        NotificationModelBuilderRegistry registry = new NotificationModelBuilderRegistry(List.of(new CreatedBuilder()));

        NotificationModelBuilder<HoldListCreatedPayload> builder = registry.getTyped(
                NotificationType.HOLD_LIST_CREATED,
                HoldListCreatedPayload.class
        );

        assertNotNull(builder);
    }

    @Test
    void getTyped_throwsWhenPayloadTypeMismatched() {
        NotificationModelBuilderRegistry registry = new NotificationModelBuilderRegistry(List.of(new CreatedBuilder()));

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> registry.getTyped(NotificationType.HOLD_LIST_CREATED, HoldListRemovedPayload.class));

        assertTrue(error.getMessage().contains("Payload type mismatch"));
    }

    private static class CreatedBuilder implements NotificationModelBuilder<HoldListCreatedPayload> {

        @Override
        public NotificationType supportsType() {
            return NotificationType.HOLD_LIST_CREATED;
        }

        @Override
        public Class<HoldListCreatedPayload> payloadType() {
            return HoldListCreatedPayload.class;
        }

        @Override
        public Map<String, Object> buildModel(HoldListCreatedPayload payload) {
            return Map.of();
        }
    }
}
