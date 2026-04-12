---
description: How to add a complete new feature (end-to-end) following clean architecture
---

# Add New Feature Workflow

Follow these steps **in order** when creating a new feature. Use the existing Budget feature as the reference implementation.

## 1. Domain Layer (`:domain`)

### 1a. Create the domain model
- Path: `domain/src/main/java/com/example/domain/model/<Entity>.kt`
- Create a `data class` with all required fields
- No Android imports, no Room/Serialization annotations — pure Kotlin

### 1b. Create the repository interface
- Path: `domain/src/main/java/com/example/domain/repo/<Entity>Repository.kt`
- Define CRUD operations as an `interface`
- Read operations return `Flow<>`, write operations are `suspend`

### 1c. Create use cases
- Path: `domain/src/main/java/com/example/domain/usecase/<entity>_usecases/<Action><Entity>UseCase.kt`
- One use case per action (Get, Add, Update, Delete, GetById)
- Use `operator fun invoke()` pattern
- Constructor-inject the repository interface

### 1d. Register in DomainModule
- Path: `domain/src/main/java/com/example/domain/di/DomainModule.kt`
- Add `factoryOf(::<Action><Entity>UseCase)` for each use case

---

## 2. Data Layer (`:data`)

### 2a. Create the Room entity
- Path: `data/src/main/java/com/example/data/local/model/<Entity>Entity.kt`
- Annotate with `@Entity(tableName = "<entities>")`
- Use `@PrimaryKey` on the id field

### 2b. Create the DAO
- Path: `data/src/main/java/com/example/data/local/dao/<Entity>Dao.kt`
- Annotate with `@Dao`
- `@Query` for reads (return `Flow`), `@Insert`, `@Update`, `@Delete`

### 2c. Add DAO to TrackerDatabase
- Path: `data/src/main/java/com/example/data/local/TrackerDatabase.kt`
- Add `abstract fun <entity>Dao(): <Entity>Dao`
- Add `<Entity>Entity::class` to `@Database(entities = [...])`

### 2d. Create mappers
- Path: `data/src/main/java/com/example/data/mapping/<Entity>Mapper.kt`
- Extension functions: `<Entity>Entity.toDomain()`, `<Entity>.toEntity()`
- Include list mapping extensions

### 2e. Create repository implementation
- Path: `data/src/main/java/com/example/data/repository/<Entity>RepositoryImp.kt`
- Extend the domain repository interface
- Constructor-inject `TrackerDatabase`, use DAO + mappers

### 2f. Register in DataModule
- Path: `data/src/main/java/com/example/data/di/DataModule.kt`
- Add `singleOf(::<Entity>RepositoryImp) { bind<<Entity>Repository>() }`

---

## 3. Core Layer (`:core`)

### 3a. Create feature interface
- Path: `core/src/main/java/com/example/core/navigation/features/<Name>Feature.kt`
- Extend `Feature` interface
- Declare route factory methods returning `AppRoutes`

---

## 4. App Layer (`:app`)

### 4a. Create UI model
- Path: `app/.../features/<name>/model/<Name>Ui.kt`
- UI-specific data class (may include computed fields like `spent`)

### 4b. Create UI mapper
- Path: `app/.../features/<name>/mapper/<Name>Mapper.kt`
- Extension functions: `<Entity>.to<Name>Ui()` and `<Name>Ui.to<Entity>()`

### 4c. Create routes
- Path: `app/.../features/<name>/navigation/<Name>Routes.kt`
- `sealed interface <Name>Routes : AppRoutes` with `@Serializable` variants

### 4d. Create contract
- Path: `app/.../features/<name>/<sub>/<Name>Contract.kt`
- Define `UiState` (`@Immutable`), `Event`, and `SideEffect` sealed interfaces

### 4e. Create ViewModel
- Path: `app/.../features/<name>/<sub>/<Name>ViewModel.kt`
- Follow the StateFlow + SharedFlow pattern defined in AGENT_RULES.md

### 4f. Create Screen (stateless composable)
- Path: `app/.../features/<name>/<sub>/<Name>Screen.kt`
- Receives UiState, onEvent lambda, snackBarHostState, modifier

### 4g. Create Route (stateful wrapper)
- Path: `app/.../features/<name>/<sub>/<Name>Route.kt`
- Connects ViewModel to Screen via `collectAsStateWithLifecycle()`

### 4h. Create Navigation extension
- Path: `app/.../features/<name>/<sub>/navigation/Navigation.kt`
- `NavGraphBuilder.<sub>Route(...)` extension function

### 4i. Create FeatureImpl
- Path: `app/.../features/<name>/navigation/<Name>FeatureImpl.kt`
- Implements the Feature interface from core
- Wires NavController with lambda callbacks

### 4j. Create DI module
- Path: `app/.../features/<name>/di/<Name>Module.kt`
- Register FeatureImpl with `binds arrayOf(<Name>Feature::class, Feature::class)`
- Register all ViewModels with `viewModelOf`

### 4k. Register module in App.kt
- Path: `app/.../App.kt`
- Add `<name>Module` to the `appModule` list
