package com.example.notification.domain;

import com.example.notification.model.NotificationType;
import com.example.notification.orchestration.EmailNotificationOrchestrator;
import org.springframework.stereotype.Service;

@Service
public class HoldListDomainService {

    private final EmailNotificationOrchestrator notificationOrchestrator;

    public HoldListDomainService(EmailNotificationOrchestrator notificationOrchestrator) {
        this.notificationOrchestrator = notificationOrchestrator;
    }

    public void createHoldList(String holdListName, String actor, int itemCount) {
        HoldListCreatedPayload payload = new HoldListCreatedPayload(holdListName, actor, itemCount);
        notificationOrchestrator.notify(NotificationType.HOLD_LIST_CREATED, payload);
    }

    public void removeHoldList(String holdListName, String actor, String reason) {
        HoldListRemovedPayload payload = new HoldListRemovedPayload(holdListName, actor, reason);
        notificationOrchestrator.notify(NotificationType.HOLD_LIST_REMOVED, payload);
    }
}
