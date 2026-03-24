package com.example.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class EmailNotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailNotificationsApplication.class, args);
    }
}
