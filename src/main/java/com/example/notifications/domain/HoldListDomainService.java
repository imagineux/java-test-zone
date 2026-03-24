package com.example.notifications.domain;

import com.example.notifications.config.NotificationType;
import com.example.notifications.model.HoldListCreatedPayload;
import com.example.notifications.model.HoldListRemovedPayload;
import com.example.notifications.orchestration.EmailNotificationOrchestrator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoldListDomainService {

    private final EmailNotificationOrchestrator notificationOrchestrator;

    public HoldListDomainService(EmailNotificationOrchestrator notificationOrchestrator) {
        this.notificationOrchestrator = notificationOrchestrator;
    }

    public void createHoldList(String name, String actor, List<String> impactedAccounts) {
        HoldListCreatedPayload payload = new HoldListCreatedPayload(name, actor, impactedAccounts);
        notificationOrchestrator.publish(NotificationType.HOLD_LIST_CREATED, payload, HoldListCreatedPayload.class);
    }

    public void removeHoldList(String name, String actor, String reason) {
        HoldListRemovedPayload payload = new HoldListRemovedPayload(name, actor, reason);
        notificationOrchestrator.publish(NotificationType.HOLD_LIST_REMOVED, payload, HoldListRemovedPayload.class);
    }
}
