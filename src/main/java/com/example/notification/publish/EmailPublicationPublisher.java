package com.example.notification.publish;

import com.example.notification.model.EmailPublicationRequest;

public interface EmailPublicationPublisher {
    void publish(EmailPublicationRequest request);
}
