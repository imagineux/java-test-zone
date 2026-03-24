package com.example.notifications.builder;

import com.example.notifications.domain.HoldListCreatedPayload;
import com.example.notifications.domain.NotificationType;
import com.example.notifications.model.NotificationModelBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HoldListCreatedModelBuilder implements NotificationModelBuilder<HoldListCreatedPayload> {

    @Override
    public NotificationType type() {
        return NotificationType.HOLD_LIST_CREATED;
    }

    @Override
    public Class<HoldListCreatedPayload> payloadClass() {
        return HoldListCreatedPayload.class;
    }

    @Override
    public Map<String, Object> buildModel(HoldListCreatedPayload payload) {
        return Map.of(
                "holdListName", payload.holdListName(),
                "createdBy", payload.createdBy(),
                "effectiveDate", payload.effectiveDate()
        );
    }
}
