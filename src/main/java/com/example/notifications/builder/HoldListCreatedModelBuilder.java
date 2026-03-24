package com.example.notifications.builder;

import com.example.notifications.config.NotificationType;
import com.example.notifications.model.HoldListCreatedPayload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HoldListCreatedModelBuilder implements NotificationModelBuilder<HoldListCreatedPayload> {

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
        return Map.of(
                "holdListName", payload.holdListName(),
                "createdBy", payload.createdBy(),
                "impactedAccounts", payload.impactedAccounts()
        );
    }
}
