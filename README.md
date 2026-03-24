# Spring Boot Email Notification Pattern (Publisher Only)

## 1) Pattern summary
This code uses one shared orchestration flow and tiny per-notification model builders.

Why this keeps cognitive load low:
- One entry point: `EmailNotificationOrchestrator.notify(type, payload)`.
- One place for config lookup: `NotificationCatalog`.
- One place for rendering: `TemplateRenderer`.
- One place for publishing: `EmailPublicationPublisher`.
- Notification-specific logic is isolated to small builders (`buildModel`).

## 2) Package / file structure

```text
com.example.notifications
├── NotificationApplication
├── application
│   ├── EmailNotificationOrchestrator
│   └── HoldListDomainService
├── builder
│   ├── HoldListCreatedModelBuilder
│   └── HoldListRemovedModelBuilder
├── config
│   ├── NotificationCatalog
│   ├── NotificationDefinition
│   └── NotificationProperties
├── domain
│   ├── HoldListCreatedPayload
│   ├── HoldListRemovedPayload
│   ├── NotificationPayload
│   └── NotificationType
├── model
│   ├── NotificationModelBuilder
│   └── NotificationModelBuilderRegistry
├── publish
│   ├── EmailPublicationPublisher
│   └── EmailPublicationRequest
└── render
    ├── TemplateRenderer
    └── ThymeleafTemplateRenderer
```

## 3) Main runtime flow
1. Domain service calls orchestrator once.
2. Orchestrator loads config by `NotificationType`.
3. Registry picks the matching model builder.
4. Builder converts payload -> template model.
5. Shared renderer renders template.
6. Orchestrator builds `EmailPublicationRequest`.
7. Shared publisher interface publishes message.

## 4) application.yml example
See `src/main/resources/application.yml` for two notifications (`HOLD_LIST_CREATED`, `HOLD_LIST_REMOVED`).

## 5) Template examples
See:
- `src/main/resources/templates/notifications/hold-list-created.html`
- `src/main/resources/templates/notifications/hold-list-removed.html`

## 6) Example usage from a domain service
`HoldListDomainService` shows both create/remove use cases and calls one orchestrator method.

## 7) How to add a new notification
1. Add enum value to `NotificationType`.
2. Add YAML entry under `notifications.definitions`.
3. Add Thymeleaf template under `resources/templates/...`.
4. Add payload record (optional if existing payload works).
5. Add one `NotificationModelBuilder` implementation for that type.

No change is required in `EmailNotificationOrchestrator` or shared renderer/publisher logic.

## 8) Focused unit tests
- `EmailNotificationOrchestratorTest`
- `NotificationModelBuilderRegistryTest`

## 9) Tradeoffs
This is intentionally boring and explicit:
- Better than ad hoc per-notification services because render/publish/config logic is centralized.
- Better than huge switches because type-specific logic stays in focused builders.
- Slightly less compile-time type safety in orchestrator payload parameter, but much easier day-2 maintenance and extension.
