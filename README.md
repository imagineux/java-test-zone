# Email Notification Orchestrator Pattern (Spring Boot)

A minimal, boring pattern for publishing email notification requests to another delivery service.

## Why this lowers cognitive load

- One entry point (`EmailNotificationOrchestrator`) for all notification publishing.
- One typed config map (`NotificationProperties`) for all notification definitions.
- One shared render path (`TemplateRenderer`).
- One shared publish path (`EmailPublicationPublisher`).
- Per-notification code is only a tiny `NotificationModelBuilder` that maps payload -> template model.

## Package structure

```text
com.example.notifications
├── builder
│   ├── HoldListCreatedModelBuilder
│   ├── HoldListRemovedModelBuilder
│   └── NotificationModelBuilder
├── config
│   ├── NotificationCatalog
│   ├── NotificationDefinition
│   ├── NotificationProperties
│   └── NotificationType
├── domain
│   └── HoldListDomainService
├── model
│   ├── HoldListCreatedPayload
│   ├── HoldListRemovedPayload
│   └── NotificationPayload
├── orchestration
│   ├── EmailNotificationOrchestrator
│   └── NotificationModelBuilderRegistry
├── publish
│   ├── EmailPublicationPublisher
│   ├── EmailPublicationRequest
│   └── LoggingEmailPublicationPublisher
└── render
    ├── TemplateRenderer
    └── ThymeleafTemplateRenderer
```

## Runtime flow

1. Domain service calls orchestrator.
2. Orchestrator loads notification definition from catalog.
3. Orchestrator resolves model builder from registry.
4. Builder maps payload to model.
5. Shared renderer renders Thymeleaf template.
6. Orchestrator builds `EmailPublicationRequest`.
7. Shared publisher publishes request.

## How to add a notification

1. Add enum value to `NotificationType`.
2. Add YAML config entry under `notifications.definitions`.
3. Add Thymeleaf template file under `resources/templates`.
4. Add payload record (if needed).
5. Add one `NotificationModelBuilder` for model mapping.
6. Call orchestrator from domain code.

No orchestration flow changes required.

## Tradeoff vs ad-hoc per-notification services

This pattern avoids duplicated render/publish code and avoids giant switch statements in service methods. It also avoids creating one full service pipeline per notification. You get one stable flow with small, obvious extension points.
