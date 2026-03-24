package com.example.notifications.builder;

import com.example.notifications.domain.HoldListRemovedPayload;
import com.example.notifications.domain.NotificationType;
import com.example.notifications.model.NotificationModelBuilder;
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
