package com.example.notifications.application;

import com.example.notifications.domain.HoldListCreatedPayload;
import com.example.notifications.domain.HoldListRemovedPayload;
import com.example.notifications.domain.NotificationType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HoldListDomainService {

    private final EmailNotificationOrchestrator orchestrator;

    public HoldListDomainService(EmailNotificationOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    public void createHoldList(String holdListName, String createdBy) {
        // domain behavior omitted
        orchestrator.notify(
                NotificationType.HOLD_LIST_CREATED,
                new HoldListCreatedPayload(holdListName, createdBy, LocalDate.now())
        );
    }

    public void removeHoldList(String holdListName, String removedBy, String reason) {
        // domain behavior omitted
        orchestrator.notify(
                NotificationType.HOLD_LIST_REMOVED,
                new HoldListRemovedPayload(holdListName, removedBy, reason)
        );
    }
}
