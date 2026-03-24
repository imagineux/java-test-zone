package com.example.notifications.builder;

import com.example.notifications.config.NotificationType;
import com.example.notifications.model.HoldListRemovedPayload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HoldListRemovedModelBuilder implements NotificationModelBuilder<HoldListRemovedPayload> {

    @Override
    public NotificationType supportsType() {
        return NotificationType.HOLD_LIST_REMOVED;
    }

    @Override
    public Class<HoldListRemovedPayload> payloadType() {
        return HoldListRemovedPayload.class;
    }

    @Override
    public Map<String, Object> buildModel(HoldListRemovedPayload payload) {
        return Map.of(
                "holdListName", payload.holdListName(),
                "removedBy", payload.removedBy(),
                "removalReason", payload.removalReason()
        );
    }
}
