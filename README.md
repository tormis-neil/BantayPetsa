# BantayPetsa

**Tagline:** *Bantay sa petsa. Alaga sa paninda.*

BantayPetsa is an offline-first Android inventory assistant for small retail store owners. It tracks products by expiry-dated batches, highlights upcoming and expired stock, supports stock adjustments, and keeps a history of inventory actions.

## 1. Problem

Small retail stores may rely on memory, handwritten notes, or manual checking to identify products nearing expiry. This can make it harder to prioritize stock, reduce avoidable waste, and maintain a clear record of stock changes.

## 2. Solution

Provide a simple, local-first mobile app that organizes products and their batches, classifies expiry status, prioritizes earlier-expiring stock using **FEFO** (First Expired, First Out), sends local reminders, and records stock actions. Core inventory functions must work without internet access.

## 3. Objectives

1. Let owners create, view, edit, archive, and manage products.
2. Track multiple batches per product, including quantity and expiration date.
3. Calculate product totals from batch quantities and classify expiry status using owner-configurable thresholds.
4. Help owners prioritize batches using FEFO ordering and expiry alerts.
5. Record stock additions, removals, and adjustments with before/after quantities and timestamps.
6. Provide a dashboard, searchable inventory, alerts, and action history.
7. Persist data locally and support core workflows offline.
8. Validate the app through functional tests and, where available, feedback from a small number of store owners.

## 4. Target Users

**Primary user:** A small retail store owner or operator managing stock on one device.

The MVP is designed for one local owner profile/device. It does not provide multi-user collaboration, staff roles, or multi-branch synchronization.

## 5. MVP Scope

- Local owner access protection using a PIN or suitable device-level protection.
- Product management: name, category, unit, optional description, archive state.
- Batch management: product link, quantity, expiration date, received date, optional lot number.
- Product quantity totals derived from its batches; do not store a separately editable product total.
- Expiry statuses and configurable warning thresholds.
- FEFO sorting and prioritized expiry alerts.
- Stock actions: add stock, remove stock, and adjust quantity with a reason where appropriate.
- Action history with quantity before/after, change, action type, and timestamp.
- Dashboard summaries and basic reports.
- Local notifications for relevant upcoming/expired stock conditions.
- Room-backed persistence, settings storage, and offline operation.

## 6. Limitations and Out of Scope

- No point-of-sale, payment processing, accounting, supplier ordering, or e-commerce.
- No multi-branch or multi-user features.
- No AI demand forecasting or automatic replenishment.
- No barcode scanning, OCR, camera-based counting, or required photo capture.
- No guaranteed cloud backup, cloud sync, or cross-device data recovery.
- Firebase Cloud Messaging (FCM) is optional future work, not required for the MVP.
- Expiry labels are inventory-management reminders, not food-safety certification or a guarantee that a product is safe/unsafe to consume.
- Local notification delivery depends on Android notification permission and operating-system behavior; the app must explain when notifications are disabled.

## 7. Functional Requirements (FR)

- **FR-01 — Owner access:** Protect app access using the selected local access method. Never store a PIN as plain text.
- **FR-02 — Products:** Create, view, edit, search/filter, and archive products.
- **FR-03 — Batches:** Add and manage multiple batches for a product. Validate required fields and quantity.
- **FR-04 — Derived totals:** Calculate product quantity from its active batch quantities. Keep totals consistent after every batch or stock action.
- **FR-05 — Expiry classification:** Classify each batch using its expiration date and the owner's warning thresholds.
- **FR-06 — FEFO:** Sort eligible stock by earliest expiration date; show undated/invalid data distinctly rather than silently treating it as safe.
- **FR-07 — Stock actions:** Record stock additions, removals, and adjustments. Prevent negative quantities unless a future approved requirement explicitly changes this rule.
- **FR-08 — Action history:** Store action type, quantity change, quantity before/after, timestamp, and optional reason. A stock change and its history record must succeed or fail together.
- **FR-09 — Dashboard and alerts:** Show useful inventory totals, approaching/urgent/expired batches, and clear empty states.
- **FR-10 — Notifications:** Schedule local reminders where permitted; core expiry status must remain visible in-app if notifications are unavailable.
- **FR-11 — Settings:** Let the owner configure warning thresholds and relevant preferences, with validated values and sensible defaults.
- **FR-12 — Persistence/offline:** Retain inventory and settings after app restart and support core operations without a network connection.
- **FR-13 — Reports/history:** Present basic summaries and filterable stock-action history.
- **FR-14 — Recovery and errors:** Provide understandable validation, permission, database, and operation-error states without silently losing changes.

## 8. Non-Functional Requirements (NFR)

- **NFR-01 — Correctness:** Business rules must be deterministic and unit-testable.
- **NFR-02 — Offline-first:** Room is the source of truth for inventory; network availability must not be required for core inventory operations.
- **NFR-03 — Data integrity:** Use database transactions for quantity updates and corresponding action-history records. Enforce valid relationships and non-negative quantities.
- **NFR-04 — Usability:** Use plain language, consistent navigation, clear labels, and confirmation for destructive actions.
- **NFR-05 — Accessibility:** Support font scaling, sufficient contrast, screen-reader labels, and touch targets of at least 48 × 48 dp where applicable. Never communicate status by color alone.
- **NFR-06 — Maintainability:** Keep UI, business logic, and data access separated; avoid oversized composables and duplicated business rules.
- **NFR-07 — Security:** Do not hardcode secrets, expose sensitive data in logs, or store the PIN in plain text. Request only necessary permissions.
- **NFR-08 — Reliability:** Handle empty, loading, validation, permission-denied, and failure states. Never present failed writes as successful.
- **NFR-09 — Performance:** Keep ordinary inventory lists responsive; use lazy lists for potentially long collections.
- **NFR-10 — Testability:** Provide unit tests for business rules and database/repository tests for critical persistence behavior.

## 9. Technology Requirements

- **Language:** Kotlin.
- **UI:** Jetpack Compose with Material 3.
- **Architecture:** Single-activity app; UI layer with ViewModels and immutable UI state; repositories separating UI from data sources.
- **State/concurrency:** Kotlin coroutines and Flow/StateFlow.
- **Local database:** Room for products, batches, and stock-action history.
- **Settings:** DataStore for small preferences such as expiry thresholds and display settings.
- **Notifications:** Android local notifications/notification channels, with runtime permission handling where required by the Android version.
- **Testing:** Kotlin unit tests; Room/repository tests for persistence and transaction behavior; Compose UI tests for critical flows where practical.
- **Build:** Use a stable Android Gradle Plugin/Kotlin combination compatible with the installed Android Studio. Keep dependency versions centralized and avoid unnecessary libraries.
- **Network:** No network dependency for MVP inventory features. Add FCM only after explicit approval and only as an optional extension.

## 10. Architecture and Data Model

Use a clear separation of concerns:

`Compose UI → ViewModel → Use case/domain logic (when useful) → Repository → Room/DataStore`

Keep business rules out of composables and database entities. Use immutable UI state and lifecycle-aware collection of flows.

### Core entities

- **Product:** `productId`, `name`, `category`, `unit`, optional `description`, `isArchived`, `createdAt`.
- **Batch:** `batchId`, `productId` (foreign key), optional `lotNumber`, `quantity`, `expirationDate`, `dateReceived`, `createdAt`.
- **StockAction:** `actionId`, `batchId` (foreign key), `actionType`, `quantityChange`, `quantityBefore`, `quantityAfter`, optional `reason`, `timestamp`.
- **Settings (DataStore):** owner-configurable approaching/urgent thresholds and relevant preferences.

Product totals are derived from batches, not independently editable. Use foreign keys and appropriate indexes. Update the batch quantity and insert its StockAction record in one Room transaction. Archive products instead of deleting history accidentally. If deletion is later implemented, define and test its history/foreign-key behavior first.

## 11. Expiry and Stock Rules

- Store and compare expiration dates consistently using a date-only representation; avoid time-zone shifts changing the calendar date.
- Let **D** be days from today to the batch expiration date, **U** the urgent threshold, and **A** the approaching threshold, with `U < A`.
- **Expired:** `D < 0`.
- **Expires today:** `D = 0`; treat as urgent and label explicitly.
- **Urgent:** `0 < D <= U`.
- **Approaching:** `U < D <= A`.
- **Safe/future:** `D > A`. This is only an app status label, not a safety guarantee.
- Suggested initial defaults: `U = 7 days`, `A = 30 days`. The owner can change them; validate `U >= 0` and `A > U`.
- Sort actionable batches by earliest expiration date (FEFO). Expired stock should be prominently identified rather than hidden.
- Quantities must be valid and non-negative. Removal/adjustment cannot reduce a batch below zero.
- A stock action must update quantity and history atomically.

## 12. Design System and UX

**Visual direction:** Fresh Market / Modern Utility — calm, readable, practical, and suitable for frequent use in a small shop.

### Color tokens

| Token | Hex | Use |
|---|---|---|
| Primary | `#245C45` | Main actions, app identity |
| Secondary / Sage | `#DCE9DF` | Soft supporting surfaces |
| Background | `#F7F7F2` | Main background |
| Surface | `#FFFFFF` | Cards and form surfaces |
| Text primary | `#202822` | Main text |
| Text secondary | `#5F6B62` | Supporting text |
| Approaching | `#8A5700` | Approaching-expiry status |
| Urgent / Expired | `#B42318` | Urgent and expired status |

Use Material 3 color roles and verify contrast in both light and dark themes if dark mode is implemented. Do not use color as the only status indicator; pair colors with text and/or icons.

### Navigation and screens

Bottom navigation:
- **Home:** summary cards, urgent batches, quick actions.
- **Products:** searchable/filterable product list.
- **Alerts:** approaching, urgent, expires-today, and expired batches.
- **History:** stock-action records.

Provide **Settings** through a clear app-bar action or dedicated destination. Supporting screens: product/batch details, add/edit product, add/edit batch, record stock action, and threshold settings.

### UX rules

- Use clear labels, consistent spacing, readable type, and a visible primary action.
- Use at least 48 × 48 dp touch targets where applicable.
- Use numeric keyboards for quantities and date pickers for dates.
- Show loading, empty, success, validation, permission-denied, and error states.
- Confirm destructive or consequential actions and explain what will change.
- Avoid decorative charts that obscure actionable stock information.
- Do not leave blank screens, dead buttons, fake data in production flows, or unimplemented controls presented as working features.

## 13. Code Quality and Security Rules

1. Keep one main activity and organize features and layers into understandable packages.
2. Suggested package structure:
   - `app/navigation`
   - `core/designsystem`, `core/util`, `core/time`
   - `data/local/database`, `data/local/dao`, `data/local/entity`, `data/repository`
   - `domain/model`, `domain/usecase` (when useful)
   - `feature/dashboard`, `feature/products`, `feature/batches`, `feature/alerts`, `feature/history`, `feature/settings`
   - `notifications`
3. Use a single Gradle module unless a real project need justifies more.
4. Keep composables focused; extract reusable UI components and keep business logic independently testable.
5. Use repositories, coroutines, and Flow rather than direct database access from UI components.
6. Do not duplicate expiry or stock-validation rules across screens.
7. Use Room transactions for multi-step inventory writes and handle database errors explicitly.
8. Never hardcode secrets, log PINs, or store a PIN as plain text. Use an appropriate secure verification/storage design; do not invent custom cryptography.
9. Avoid unnecessary dependencies, premature abstractions, and speculative features.
10. Do not claim a feature works unless it is implemented and tested.

## 14. Testing and Acceptance Criteria

At minimum, test:
- Product create/edit/search/archive.
- Multiple batches and correct derived product totals.
- Valid and invalid quantities; removal cannot create a negative balance.
- Stock action history contains correct before/after values and timestamps.
- Batch quantity and history remain consistent if a database write fails.
- Expiration yesterday, today, urgent boundary, approaching boundary, and safe boundary.
- Threshold validation and changes.
- FEFO ordering and expired-stock visibility.
- Data survives app restart and core workflows work in airplane mode.
- Local notification behavior when permission is granted and when denied.
- Empty, error, and invalid-form states.

**MVP acceptance gate:** The app builds and launches; all MVP flows are connected to persistent local data; core inventory operations work offline; stock history and quantities remain consistent; expiry rules pass boundary tests; and no visible control falsely implies an implemented feature.

Evaluation should combine functional testing with feedback from a small number of store owners if participants are available. Record feedback and resulting fixes; do not claim a larger study than was conducted.

## 15. Six-Phase Development Plan

**Do not create more than these six phases.** Each phase must end with a successful build, relevant tests, a short change summary, and a list of known issues. Do not proceed until the phase gate is met.

### Phase 1 — Foundation and Project Setup
- Confirm package name, minimum SDK, compatible toolchain, dependencies, and project structure.
- Configure Compose, Material 3 theme tokens, navigation skeleton, and basic app shell.
- **Gate:** App builds and launches; navigation skeleton and theme are visible; no avoidable build warnings/errors remain unexplained.

### Phase 2 — UI/UX and Core Screens
- Implement navigation and screen layouts with reusable components and realistic preview/sample states.
- Add accessible forms, validation presentation, empty/loading/error states, and responsive lists.
- **Gate:** Every planned screen is reachable; controls have defined behavior; no dead buttons or misleading success states.

### Phase 3 — Inventory and Expiry Logic
- Implement product/batch domain rules, quantity validation, FEFO ordering, expiry classification, and stock-action logic.
- Write unit tests for boundary conditions before connecting all UI flows.
- **Gate:** Business-rule tests pass; invalid quantity changes are rejected; totals are derived consistently.

### Phase 4 — Local Data and Offline Persistence
- Implement Room entities, DAOs, repositories, transactions, and DataStore settings.
- Connect screens to persistent state and test restart/airplane-mode behavior.
- **Gate:** CRUD and stock actions persist; batch quantity and action history update atomically; core inventory workflows work offline.

### Phase 5 — Notifications, Security, and Integration
- Implement local notification channels, permission handling, reminder scheduling, and the selected owner access protection.
- Integrate and validate the end-to-end workflow. FCM remains out of scope unless separately approved.
- **Gate:** Core alerts remain available in-app even if notifications are disabled; access protection and notification behavior are tested; no PIN or secret is exposed.

### Phase 6 — Testing, Refinement, and Release
- Run regression, persistence, boundary, accessibility, and critical UI tests.
- Gather small-store-owner feedback if available, fix defects, and finalize README/setup instructions.
- Produce a release-ready build and document remaining limitations.
- **Gate:** Acceptance criteria pass, known issues are documented, and the final build is reproducible.

## 16. AI Coding Workflow and Rules

Treat this README as the source of truth. If a requirement is ambiguous, ask before making a consequential assumption. Do not silently expand scope.

For every task:
1. State the specific requirement(s) being implemented and the files likely to change.
2. Inspect the current project structure and existing code before editing.
3. Implement only the current phase/task; avoid unrelated rewrites.
4. Explain important design choices briefly.
5. Run the relevant build and tests where tool access allows; report actual results, not assumed results.
6. Summarize changed files, validation performed, failures, and remaining work.
7. Stop at the phase gate and wait for approval before starting the next phase.

Never generate the whole app in one unreviewed pass. Never replace working code without explaining why. Never fabricate test results, silently remove requirements, or add cloud services/features without approval. Prefer small, compilable increments.

## 17. Official References

- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [Android architecture recommendations](https://developer.android.com/topic/architecture/recommendations)
- [Offline-first architecture](https://developer.android.com/topic/architecture/data-layer/offline-first)
- [Room persistence library](https://developer.android.com/training/data-storage/room)
- [Testing Room databases](https://developer.android.com/training/data-storage/room/testing-db)
- [Material 3 in Jetpack Compose](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Android accessibility guidance: touch targets](https://support.google.com/accessibility/android/answer/7101858)
- [Android security tips](https://developer.android.com/privacy-and-security/security-tips)

---

**Instruction to the coding AI:** Build BantayPetsa as a maintainable, offline-first Android app according to this README. The six phases and their gates are mandatory. Implement only approved scope, validate each increment, and ask for approval before advancing between phases.
