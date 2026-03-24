package com.example.notification.service;

import com.example.notification.domain.NotificationType;
import com.example.notification.model.HoldListCreatedPayload;
import com.example.notification.model.HoldListRemovedPayload;
import com.example.notification.orchestration.EmailNotificationOrchestrator;
import org.springframework.stereotype.Service;

@Service
public class HoldListDomainService {

    private final EmailNotificationOrchestrator notificationOrchestrator;

    public HoldListDomainService(EmailNotificationOrchestrator notificationOrchestrator) {
        this.notificationOrchestrator = notificationOrchestrator;
    }

    public void createHoldList(String holdListId, String actor, int itemCount) {
        HoldListCreatedPayload payload = new HoldListCreatedPayload(holdListId, actor, itemCount);
        notificationOrchestrator.notify(NotificationType.HOLD_LIST_CREATED, payload);
    }

    public void removeHoldList(String holdListId, String actor, String reason) {
        HoldListRemovedPayload payload = new HoldListRemovedPayload(holdListId, actor, reason);
        notificationOrchestrator.notify(NotificationType.HOLD_LIST_REMOVED, payload);
    }
}
