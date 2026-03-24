package com.example.notification.orchestration;

import com.example.notification.domain.NotificationType;
import com.example.notification.model.HoldListCreatedPayload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HoldListCreatedModelBuilder implements NotificationModelBuilder<HoldListCreatedPayload> {

    @Override
    public NotificationType supports() {
        return NotificationType.HOLD_LIST_CREATED;
    }

    @Override
    public Class<HoldListCreatedPayload> payloadType() {
        return HoldListCreatedPayload.class;
    }

    @Override
    public Map<String, Object> buildModel(HoldListCreatedPayload payload) {
        return Map.of(
                "holdListId", payload.holdListId(),
                "createdBy", payload.createdBy(),
                "itemCount", payload.itemCount()
        );
    }
}
