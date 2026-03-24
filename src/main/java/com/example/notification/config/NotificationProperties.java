package com.example.notification.config;

import com.example.notification.domain.NotificationType;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "notifications")
public class NotificationProperties {

    private final Map<NotificationType, NotificationDefinition> email = new EnumMap<>(NotificationType.class);

    public Map<NotificationType, NotificationDefinition> getEmail() {
        return email;
    }

    public static class NotificationDefinition {
        private List<String> recipients;
        private String templatePath;
        private String subject;
        private boolean enabled = true;

        public List<String> getRecipients() {
            return recipients;
        }

        public void setRecipients(List<String> recipients) {
            this.recipients = recipients;
        }

        public String getTemplatePath() {
            return templatePath;
        }

        public void setTemplatePath(String templatePath) {
            this.templatePath = templatePath;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
