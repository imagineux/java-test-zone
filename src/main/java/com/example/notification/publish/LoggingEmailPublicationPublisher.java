package com.example.notification.publish;

import com.example.notification.model.EmailPublicationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingEmailPublicationPublisher implements EmailPublicationPublisher {

    private static final Logger log = LoggerFactory.getLogger(LoggingEmailPublicationPublisher.class);

    @Override
    public void publish(EmailPublicationRequest request) {
        // Placeholder implementation. Replace with Kafka/topic producer adapter in real deployment.
        log.info("Publishing email request: recipients={}, subject={}", request.recipients(), request.subject());
    }
}
