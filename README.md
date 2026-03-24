# Spring Boot Email Notification Pattern (Publish-Only)

This project demonstrates a **boring, low-cognitive-load** notification pattern:

- one orchestration flow for render + publish
- one config catalog for YAML lookup
- one builder per notification type for payload-to-template-model mapping
- shared renderer and shared publisher interfaces

The service **does not send email directly**. It only creates and publishes an `EmailPublicationRequest` to another delivery service.

## Why this pattern keeps cognitive load low

- You call a single entry point (`EmailNotificationOrchestrator.notify(...)`).
- Shared concerns (config lookup, rendering, publishing, enabled checks) live in one place.
- Notification-specific code stays tiny and local (just model builders + optional payload records).
- No giant switch in domain services and no ad hoc per-notification pipelines.

## Package structure

```text
src/main/java/com/example/notification
├── NotificationApplication.java
├── config
│   ├── NotificationCatalog.java
│   └── NotificationProperties.java
├── domain
│   └── NotificationType.java
├── model
│   ├── EmailPublicationRequest.java
│   ├── HoldListCreatedPayload.java
│   └── HoldListRemovedPayload.java
├── orchestration
│   ├── EmailNotificationOrchestrator.java
│   ├── HoldListCreatedModelBuilder.java
│   ├── HoldListRemovedModelBuilder.java
│   └── NotificationModelBuilder.java
├── publish
│   ├── EmailPublicationPublisher.java
│   └── LoggingEmailPublicationPublisher.java
├── render
│   ├── TemplateRenderer.java
│   └── ThymeleafTemplateRenderer.java
└── service
    └── HoldListDomainService.java
```

## Runtime flow

1. Domain service calls orchestrator `notify(type, payload)`.
2. Orchestrator loads config from `NotificationCatalog`.
3. Orchestrator resolves builder for `NotificationType`.
4. Builder converts payload -> template model.
5. Shared `TemplateRenderer` renders HTML.
6. Orchestrator builds `EmailPublicationRequest`.
7. Shared `EmailPublicationPublisher` publishes it.

## Configuration example

See `src/main/resources/application.yml`.

## Template examples

See:

- `src/main/resources/templates/hold-list-created.html`
- `src/main/resources/templates/hold-list-removed.html`

## Example usage

See `HoldListDomainService` for how business logic triggers notifications through one orchestrator entry point.

## How to add a new notification type

1. Add a new enum value in `NotificationType`.
2. Add YAML config under `notifications.email.<TYPE>` in `application.yml`.
3. Add template file in `resources/templates`.
4. Add payload record (optional but recommended for type clarity).
5. Add one `NotificationModelBuilder<T>` component for the new type.
6. Start using `orchestrator.notify(NEW_TYPE, payload)` from domain service.

No shared orchestration changes should be needed.

## Focused tests

- `NotificationCatalogTest`: config lookup + missing config behavior.
- `EmailNotificationOrchestratorTest`: happy path publish, disabled behavior, payload mismatch guard.

## Tradeoffs

This pattern intentionally prefers explicit wiring over dynamic magic:

- **Pros:** easy to trace, easy to debug, simple onboarding, low day-2 maintenance.
- **Cons:** one small builder class per notification type (a bit more boilerplate).

Given maintainability goals, this is a good trade.
