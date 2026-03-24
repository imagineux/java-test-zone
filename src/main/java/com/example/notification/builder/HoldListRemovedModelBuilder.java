package com.example.notification.builder;

import com.example.notification.domain.HoldListRemovedPayload;
import com.example.notification.model.NotificationType;
import com.example.notification.orchestration.NotificationModelBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HoldListRemovedModelBuilder implements NotificationModelBuilder<HoldListRemovedPayload> {

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
        return Map.of(
                "holdListName", payload.holdListName(),
                "removedBy", payload.removedBy(),
                "reason", payload.reason()
        );
    }
}
