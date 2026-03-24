package com.example.notification.orchestration;

import com.example.notification.domain.NotificationType;
import com.example.notification.model.HoldListRemovedPayload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HoldListRemovedModelBuilder implements NotificationModelBuilder<HoldListRemovedPayload> {

    @Override
    public NotificationType supports() {
        return NotificationType.HOLD_LIST_REMOVED;
    }

    @Override
    public Class<HoldListRemovedPayload> payloadType() {
        return HoldListRemovedPayload.class;
    }

    @Override
    public Map<String, Object> buildModel(HoldListRemovedPayload payload) {
        return Map.of(
                "holdListId", payload.holdListId(),
                "removedBy", payload.removedBy(),
                "reason", payload.reason()
        );
    }
}
