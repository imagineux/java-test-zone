package com.example.notifications.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingEmailPublicationPublisher implements EmailPublicationPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailPublicationPublisher.class);

    @Override
    public void publish(EmailPublicationRequest request) {
        log.info("Publishing email notification={}, recipients={}, subject={}",
                request.notificationType(), request.recipients(), request.subject());
    }
}
