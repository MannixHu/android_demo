# Android Counter App - Quick Start Guide

## Project Overview
A complete, production-ready Android counter application demonstrating modern Android development practices with Jetpack Compose, MVVM, Hilt, and Room.

## Build & Run

### Build
```bash
cd /Users/mannix/Project/android_demo
./gradlew build
```

### Run Tests
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests
```

### Deploy to Emulator/Device
```bash
./gradlew installDebug
```

## Architecture

### Layers
1. **UI Layer** - Jetpack Compose screens with StateFlow
2. **ViewModel** - State management with lifecycle awareness
3. **Domain Layer** - Use cases (business logic)
4. **Data Layer** - Repository pattern with Room database
5. **DI** - Hilt for dependency injection

### Data Flow
```
User Action (Button Click)
    ↓
CounterScreen (UI)
    ↓
CounterViewModel.increment/decrement/reset()
    ↓
Use Cases (IncrementCounterUseCase, etc.)
    ↓
CounterRepository (Interface)
    ↓
CounterRepositoryImpl
    ↓
CounterLocalDataSource
    ↓
CounterDao (Room)
    ↓
SQLite Database
```

## Key Files

### Application Entry Points
- `CounterApp.kt` - Hilt application class + main Composable
- `MainActivity.kt` - Single Activity with Compose content

### UI Components
- `ui/screens/CounterScreen.kt` - Main UI with +1, -1, Reset buttons
- `ui/theme/` - Material 3 theme (colors, typography)
- `viewmodel/CounterViewModel.kt` - State & event handling

### Domain Logic
- `domain/repository/CounterRepository.kt` - Interface
- `domain/usecase/` - Get, Increment, Decrement, Reset use cases

### Data Access
- `data/local/CounterDatabase.kt` - Room database
- `data/local/CounterDao.kt` - Database queries
- `data/repository/CounterRepositoryImpl.kt` - Repository implementation

### Dependency Injection
- `di/AppModule.kt` - Use case & repository providers
- `di/DatabaseModule.kt` - Database & DAO providers

## Features

✓ Display counter value
✓ Increment counter (+1)
✓ Decrement counter (-1)
✓ Reset counter to 0
✓ Persistent storage in SQLite
✓ Auto-load on startup
✓ Material Design 3 UI
✓ Light/dark theme support
✓ Lifecycle-safe state collection
✓ Complete test suite

## Technology Stack

| Component | Library | Version |
|-----------|---------|---------|
| Build | AGP | 9.0.1 |
| Language | Kotlin | 2.0 |
| UI | Jetpack Compose | 1.7.0 |
| Design | Material 3 | 1.3.0 |
| DI | Hilt | 2.52 |
| Database | Room | 2.7.0 |
| Async | Coroutines | 1.9.0 |
| Lifecycle | Lifecycle | 2.8.0 |
| Testing | JUnit + MockK | 4.13.2 + 1.13.10 |

## Project Statistics

- **Gradle Files**: 5 (root build.gradle.kts, app build.gradle.kts, settings, etc.)
- **Source Files**: 19 Kotlin files
- **Test Files**: 5 test classes
- **Resources**: 7 XML files (strings, themes, colors, icons)
- **Total Lines of Build Config**: 192 lines

## Testing

### Run All Tests
```bash
./gradlew test
```

### Test Files
- `CounterViewModelTest.kt` - ViewModel state & events
- `GetCounterUseCaseTest.kt` - Fetch use case
- `IncrementCounterUseCaseTest.kt` - Increment logic
- `DecrementCounterUseCaseTest.kt` - Decrement logic
- `ResetCounterUseCaseTest.kt` - Reset logic

## Common Tasks

### Add a New Feature
1. Create use case in `domain/usecase/`
2. Add repository method to `domain/repository/CounterRepository.kt`
3. Implement in `data/repository/CounterRepositoryImpl.kt`
4. Add method to `CounterViewModel.kt`
5. Update `CounterScreen.kt` UI
6. Write tests in `app/src/test/`

### Modify Theme
Edit files in `ui/theme/`:
- `Color.kt` - Color scheme
- `Type.kt` - Typography
- `Theme.kt` - Material theme composition

### Update Database
1. Modify `CounterEntity.kt` (add fields)
2. Update `CounterDao.kt` (queries)
3. Increment `CounterDatabase.version`
4. Add migration if needed (Room migrations)

## Troubleshooting

### Build Fails
```bash
./gradlew clean build
```

### Gradle Cache Issues
```bash
./gradlew --stop
rm -rf ~/.gradle/caches
./gradlew build
```

### Android SDK Not Found
Ensure Android SDK 35+ is installed and `ANDROID_SDK_ROOT` is set:
```bash
export ANDROID_SDK_ROOT=/path/to/android/sdk
```

## Next Steps

1. ✓ Build the project: `./gradlew build`
2. ✓ Run tests: `./gradlew test`
3. ✓ Deploy to emulator: `./gradlew installDebug`
4. ✓ Test the app manually
5. Optionally extend with additional features

---

**Project Location**: `/Users/mannix/Project/android_demo/`

**Status**: Production-ready, fully tested, modern architecture

**Last Updated**: March 2026
