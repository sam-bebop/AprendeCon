# PrendeCon Tutoring App Implementation Plan

We will build the **PrendeCon** university tutoring application based on the 12 screens and navigation flow provided in the UI design spec.

## User Review Required

> [!IMPORTANT]
> The app will be built fully in **Jetpack Compose** with **Material 3**, replicating the exact visual hierarchy, color palette (deep indigo/purple branding), bottom navigation, and screen flows shown in the design.

> [!NOTE]
> State management and navigation will be implemented using a robust reactive architecture (`ViewModel` + `StateFlow` + Jetpack Navigation) with sample mock data so all 12 screens are fully interactive and navigable.

## Proposed Changes

### Data & Navigation Architecture
- Create domain data classes for `Tutor`, `Subject`, `TutoringSession`, and `UserProfile`.
- Implement `PrendeConViewModel` managing the app state (auth state, search filters, booked sessions).
- Set up a clean navigation graph supporting the complete user journey:
  1. Splash -> Login -> Register -> Home (with Bottom Nav)
  2. Search / Filters -> Tutor Profile -> Request Tutoring -> Confirmation -> My Sessions
  3. User Profile -> Online Session view -> Presential Session view

### UI Components & Screens (`com.example.aprendecon`)

#### [NEW] [Theme.kt & Color.kt Updates](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/ui/theme/Color.kt)
- Define PrendeCon brand colors (deep purple/indigo primary, soft backgrounds, accent buttons).

#### [NEW] [Models.kt](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/model/Models.kt)
- Data structures for tutors, subjects, sessions, and user state.

#### [NEW] [PrendeConViewModel.kt](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/PrendeConViewModel.kt)
- View model holding mock data and state actions (login, filter search, book session).

#### [NEW] [Screens (Splash, Login, Register)](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/ui/auth/AuthScreens.kt)
- **Splash / Bienvenida** (Screen 1)
- **Inicio de sesión** (Screen 2)
- **Registro** (Screen 3)

#### [NEW] [Main Bottom Nav Screens (Home, Search, Sessions, Profile)](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/ui/main/MainScreens.kt)
- **Pantalla principal** (Screen 4)
- **Búsqueda / Filtros** (Screen 5)
- **Mis tutorías** (Screen 9)
- **Perfil de usuario** (Screen 10)

#### [NEW] [Booking & Detail Screens (Tutor Profile, Request, Confirmation)](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/ui/booking/BookingScreens.kt)
- **Perfil del tutor** (Screen 6)
- **Solicitar tutoría** (Screen 7)
- **Confirmar tutoría** (Screen 8)

#### [NEW] [Session Detail Screens (Online, Presential)](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/ui/session/SessionScreens.kt)
- **Vista en línea** (Screen 11)
- **Vista presencial** (Screen 12)

#### [MODIFY] [MainActivity.kt](file:///C:/Users/samis/AndroidStudioProjects/AprendeCon2/app/src/main/java/com/example/aprendecon/MainActivity.kt)
- Wire up the navigation controller and set the start destination to Splash.

## Verification Plan

### Automated Tests
- Build verification via Gradle (`app:assembleDebug`) to ensure zero compilation errors.

### Manual Verification
- Deploy and run the app on the connected Android emulator/device to walk through all 12 screens and flows interactively.
