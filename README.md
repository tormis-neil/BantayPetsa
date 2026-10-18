# BantayPetsa

**Tagline:** *Bantay sa petsa. Alaga sa paninda.*

BantayPetsa is an offline-first Android inventory and expiration monitoring assistant designed for small Philippine retail stores (sari-sari stores, minimarts, and micro-groceries). It tracks products by expiry-dated batches, highlights upcoming, urgent, and expired stock, supports simple stock actions, maintains a traceable audit trail, and provides basic action reports -- all operational without internet access.

---

## 1. Problem Statement

Small retail store owners in the Philippines typically rely on memory, handwritten notebooks, or occasional manual shelf checking to monitor product expiration dates. This manual process causes several critical problems:
- Store owners overlook items on shelves, resulting in expired goods remaining available and inadvertently sold to customers.
- Products with different expiration dates are difficult to prioritize (lack of First Expired, First Out / FEFO stock turnover).
- Expired products lead to direct financial losses (*tapon* / wastage) and severe loss of customer trust.
- Owners lack real-time visibility into which items need urgent sale, discounting, or return to distributor.
- There is no centralized record or audit trail of actions taken on near-expiry or expired inventory.

---

## 2. Proposed Solution

Provide a simple, local-first Android mobile application that automates the core retail expiry cycle:

$$\text{Record} \longrightarrow \text{Monitor} \longrightarrow \text{Alert} \longrightarrow \text{Prioritize (FEFO)} \longrightarrow \text{Act} \longrightarrow \text{History \& Reports}$$

The app organizes products into expiry-dated batches, automatically computes days remaining, classifies expiry status into 4 clear operational tiers, prioritizes earlier-expiring stock using **FEFO**, sends local reminders, and logs all stock changes with before/after balances.

---

## 3. Project Objectives

### 3.1 General Objective
To develop a mobile product expiration monitoring and stock action system that helps small retail stores record, monitor, and act on products approaching or past their expiration dates.

### 3.2 Specific Objectives
1. **Product & Batch Registration:** Provide a frictionless way to register products, quantities, expiration dates, and optional batch/lot details.
2. **Automatic Expiry Calculation:** Automatically compute days remaining and classify items into 4 operational tiers (**Safe**, **Approaching Expiry**, **Urgent**, and **Expired**).
3. **In-App Alerts & Notifications:** Provide real-time in-app alert badges, dashboard metrics, and local device notifications.
4. **FEFO Stock Prioritization:** Highlight and sort batches by earliest expiration date so owners know what to sell first.
5. **Stock Action Recording:** Allow owners to record actions: Deduct/Sold, Add/Restock, Return to Supplier, Remove/Dispose Expired, or Set/Audit.
6. **Centralized History & Basic Reports:** Maintain an audit trail of stock actions and present basic action summaries.
7. **Offline-First Persistence:** Ensure 100% functionality without internet access, storing data reliably on-device using Room.
8. **Simple Owner Security:** Protect owner settings and inventory using local authentication (PIN or biometric).

---

## 4. Scope Boundaries (Will NOT Build)

To preserve simplicity, responsiveness, and zero-friction store operation, the application strictly excludes:
- [x] **No POS / Checkout / Cash Register:** Not a point-of-sale; no cash drawer or receipt printing.
- [x] **No GCash / Maya / Payment Gateways:** No payment processing or customer wallet integration.
- [x] **No Full Accounting / Ledger System:** Focus is strictly on stock quantities and expiry dates.
- [x] **No Supplier Procurement / B2B Ordering:** Does not replace vendor purchasing channels.
- [x] **No Multi-Branch / Enterprise Cloud Sync:** Single-store local architecture.
- [x] **No AI Demand Forecasting:** Simple, transparent FEFO rules instead of black-box algorithms.
- [x] **No Barcode / OCR Mandatory Hardware:** Fast manual entry with smart defaults.

---

## 5. Expiry Status Logic (4-Tier Operational Model)

The application classifies every batch into 4 operational tiers based on days remaining ($D$):

| Status | Threshold Rule | Color Token | Operational Meaning | Action Required |
|---|---|---|---|---|
| **Safe** | $D > A$ ($> 30$ days default) | `PrimaryGreen` (`#4F8A60`) | Normal inventory | Regular shelf monitoring |
| **Approaching Expiry** | $U < D \le A$ ($8 \text{ to } 30$ days) | `AmberApproaching` (`#D97706`) | Early warning period | Review and plan action (move to front shelf) |
| **Urgent** | $0 \le D \le U$ ($0 \text{ to } 7$ days) | `OrangeAlert` (`#E87821`) | Immediate attention | Prioritize for immediate sale, bundle, or discount |
| **Expired** | $D < 0$ (Past expiration date) | `ExpenseRed` (`#E94F55`) | Date has passed | Remove from active shelf, return to distributor, or dispose |

*Defaults: Warning Period ($A$) = 30 days, Urgent Period ($U$) = 7 days. Configurable in Settings.*

---

## 6. Academic Milestone Structure: MCO 1 vs. MCO 2

The project development is structured across two academic milestones: **MCO 1** (Midterm Scope) and **MCO 2** (Final Scope).

```
+------------------------------------------------------------------------+
|                        PROJECT MILESTONE ROADMAP                       |
+-----------------------------------+------------------------------------+
                                    |
    +-------------------------------+-------------------------------+
    |                                                               |
    v                                                               v
[MCO 1: Midterm Milestone]                              [MCO 2: Final Milestone]
UI/UX, Navigation, ViewModels,                          Room Database, DataStore,
UiState, Lifecycle, and Domain Logic                    Notifications, Security, & Release
- Kotlin 2.0                                            - Room SQLite Database
- Jetpack Compose & Material 3                          - Entities, DAOs, Relations
- Navigation Compose (5 tabs + sub-flows)               - Room Transactions & Migrations
- ViewModels & Immutable UiState                        - DataStore Preferences
- Lifecycle-Aware Collection                            - Android Notification Channels
- Pure Domain Engine & Unit Tests                       - Local PIN Access Protection
(Phases 1, 2, 2-Rev, & 3)                               (Phases 4, 5, & 6)
```

---

## 7. MCO 1 - Midterm Milestone: UI, Architecture & Domain Logic

### 7.1 MCO 1 Technical Requirements Checklist

| Technical Requirement | Implementation Standard | Status |
|---|---|:---:|
| **1. Kotlin** | 100% modern Kotlin (v2.0.21), data classes, sealed classes, coroutines, lambdas, standard functions. | COMPLETED |
| **2. Jetpack Compose** | Declarative Compose UI, Foundation, Compose Animations, Material 3 tokens, layout components. | COMPLETED |
| **3. Material 3** | Material 3 Theme (`BantayPetsaTheme`), Tarsi green palette, typography scale, `Scaffold`, `TopAppBar`, `NavigationBar`, `Card`, `FilterChip`, `Button`, badges. | COMPLETED |
| **4. Nav Screen** | Jetpack Navigation Compose with 5 bottom tabs (`Home`, `Products`, `Alerts`, `History`, `Profile`) + `SignIn`, `Splash`, `ProductDetail`, `AddEditBatch`, `RecordStockAction` with backstack handling. | COMPLETED |
| **5. UI State** | Unidirectional Data Flow (UDF) with explicit, immutable `UiState` data classes representing screen states. | COMPLETED |
| **6. ViewModel Logic** | Dedicated `androidx.lifecycle.ViewModel` subclasses decoupling UI presentation from state mutation. | COMPLETED |
| **7. Lifecycle-Aware** | State collection using `collectAsStateWithLifecycle()` from `lifecycle-runtime-compose` to pause/resume collection and preserve state across rotation. | COMPLETED |

### 7.2 Phases Included in MCO 1

#### Phase 1 - Foundation & Project Setup [COMPLETED]
- Project package structure, Gradle dependencies, version catalog (`libs.versions.toml`).
- Material 3 theme tokens, typography hierarchy, initial navigation shell.
- **Gate:** Passed. App builds and launches cleanly.

#### Phase 2 - UI/UX, Navigation & Core Screens [COMPLETED]
- Screen layouts: Dashboard, Products, Alerts, History, Settings, Product Detail, Batch intake, Record Stock Action.
- Zero-friction Auth Screen (Sign In / Sign Up) and Store Owner Profile tab.
- Concept 3 Logo (*Sari-Sari Canopy & Verified Check*) and choreographed 1.4s animated splash screen.
- **Gate:** Passed. All planned screens reachable with defined interactions.

#### Phase 2 Revision - Alignment with Project Proposal [COMPLETED]
- Aligned expiry classification to the 4-tier model (`Safe`, `Approaching`, `Urgent`, `Expired`).
- Added `Urgent` badge styling and filter chip.
- Integrated the **"Monthly Action & Expiry Report"** card inside History screen with dynamic time filtering (`All Time`, `Last 7 Days`, `Last 30 Days`).
- Fixed visual padding cushions to prevent text badge collisions.
- **Gate:** Passed. 4 statuses functional across Dashboard, Alerts, Badges, and History Reports.

#### Phase 2 Architectural Completion - ViewModels, UI State & Lifecycle [COMPLETED]
- Added `lifecycle-viewmodel-compose` and `lifecycle-runtime-compose` dependencies.
- Implemented dedicated ViewModels: `DashboardViewModel`, `ProductsViewModel`, `AlertsViewModel`, `HistoryViewModel`, `ProfileViewModel`.
- Defined formal, immutable `UiState` models for each screen with unidirectional data flow.
- Refactored composables to collect state via `collectAsStateWithLifecycle()`.
- **Gate:** Passed. Screens consume state strictly from ViewModels; all 7 MCO 1 technical requirements pass.

#### Phase 3 - Pure Inventory & Expiry Domain Logic [COMPLETED]
- Pure Kotlin domain layer: `ExpiryCalculator`, `ExpiryThresholds`, `FefoEngine`, `StockInventoryEngine`, `DomainValidators`.
- Formal quantity arithmetic and mathematical invariants ($Q_{after} = Q_{before} + \Delta Q \ge 0$).
- Deterministic FEFO sorting algorithm (earliest expiry first, oldest intake tie-breaker, batchId fallback).
- Automated JVM unit tests for all boundary conditions (yesterday, today, urgent threshold, approaching threshold, safe, negative prevention).
- **Gate:** Passed. 34/34 automated unit tests pass (100% success rate); business logic is mathematically sound and verified.

---

## 8. MCO 2 - Final Milestone: Persistence, Notifications, Security & Release

### 8.1 MCO 2 Technical Requirements Checklist

| Technical Requirement | Implementation Standard | Status |
|---|---|:---:|
| **1. Room Database** | Local SQLite persistence via Room library (`@Entity`, `@Dao`, `@Database`, `@Transaction`). | UPCOMING |
| **2. DataStore** | Preferences DataStore storing owner-configurable warning ($A$) and urgent ($U$) thresholds. | UPCOMING |
| **3. Local Notifications** | Android Notification Channels, runtime permission handling, and scheduled daily background reminders. | UPCOMING |
| **4. Owner Security** | Local PIN/biometric authentication protecting sensitive inventory actions and settings without storing plain text. | UPCOMING |
| **5. End-to-End Persistence** | Complete offline data lifecycle: data survives process death, device reboots, and airplane mode. | UPCOMING |

### 8.2 Phases Included in MCO 2

#### Phase 4 - Local Data and Offline Persistence (Room DB & DataStore)
- Room entities: `ProductEntity`, `BatchEntity`, `StockActionEntity`.
- Room DAOs with Flow queries and atomic `@Transaction` writes for stock updates + action logging.
- DataStore preferences for alert thresholds.
- Repository layer connecting ViewModels to persistent Room state.
- **Gate:** Data persists across app restarts, reboots, and airplane mode; stock actions execute atomically.

#### Phase 5 - Local Notifications, Security & Integration
- Android Notification Channels for expiry alerts.
- Runtime notification permission handling with graceful degradation (in-app alerts always visible).
- Owner access PIN protection with encrypted local storage.
- End-to-end integration across all screens, repositories, and notification services.
- **Gate:** Expiry reminders trigger reliably offline; PIN protection active; zero plain-text secrets.

#### Phase 6 - Testing, Refinement & Final Release
- Full regression testing, edge-case validation, accessibility checks, and UI polish.
- Final release APK build assembly and deployment instructions.
- Complete user manual and technical documentation.
- **Gate:** Reproducible, release-ready APK validated against all acceptance criteria.

---

## 9. Design System & UI Architecture

### 9.1 Color System (Tarsi Palette)
- **Primary Green:** `#4F8A60` (Brand identity, primary actions, safe status)
- **Pale Green:** `#E8F0E9` (Pill containers, subtle highlights, active bottom nav indicator)
- **Soft White / Canvas:** `#F7F9F7` (Calm, clean screen background)
- **Surface White:** `#FFFFFF` (Card surfaces, dialogs)
- **Charcoal Green:** `#19211D` (High-contrast typography)
- **Muted Green:** `#718277` (Secondary labels, timestamps, metadata)
- **Border Subtle:** `#E2E8E2` (Clean 1dp dividers and card outlines)
- **Urgent Orange:** `#E87821` (Urgent 0 - 7 day badge and highlights)
- **Approaching Amber:** `#D97706` (Approaching expiry 8 - 30 day amber)
- **Expense / Expired Red:** `#E94F55` (Expired badges, destructive actions, sign out)

### 9.2 Navigation Structure
- **Launch Flow:** Animated Splash Screen (`SplashScreen`) $\longrightarrow$ Zero-Friction `AuthScreen` (Sign In / Sign Up)
- **Bottom Navigation Bar (5 Core Tabs):**
  1. **Home:** Overview cards, 4-tier expiry breakdown, active catalog strip, FEFO urgent attention list, quick actions.
  2. **Products:** Searchable/filterable inventory list with category chips and batch counters.
  3. **Alerts:** Filterable tab list (`All`, `Expired`, `Urgent`, `Approaching`, `Safe`) with direct stock-action buttons.
  4. **History & Reports:** Top "Monthly Action & Expiry Report" summary card + chronological audit trail.
  5. **Profile:** Store identity banner (*Aling Nena*), edit store info, archive account, data reset, and sign out.
- **Supporting Screens:** Product Detail, Add/Edit Product, Add/Edit Batch, Record Stock Action, Settings.
