---
description: How to add a new use case to the domain layer with proper DI wiring
---

# Add Use Case Workflow

## 1. Identify the repository method
- Check if the repository interface in `domain/src/.../repo/<Entity>Repository.kt` already has the method you need
- If not, add it to the interface AND the implementation in `data/src/.../repository/<Entity>RepositoryImp.kt`

## 2. Create the use case class

Path: `domain/src/main/java/com/example/domain/usecase/<entity>_usecases/<Action><Entity>UseCase.kt`

```kotlin
package com.example.domain.usecase.<entity>_usecases

import com.example.domain.repo.<Entity>Repository

class <Action><Entity>UseCase(
    private val <entity>Repository: <Entity>Repository
) {
    // For read operations returning a Flow:
    operator fun invoke(/* params */) = <entity>Repository.<method>(/* params */)

    // For write operations:
    suspend operator fun invoke(/* params */) = <entity>Repository.<method>(/* params */)
}
```

**Naming convention**: `Get<Entity>sUseCase`, `Add<Entity>UseCase`, `Delete<Entity>UseCase`, `Update<Entity>UseCase`, `Get<Entity>ByIdUseCase`

## 3. Register in DomainModule

Add to `domain/src/main/java/com/example/domain/di/DomainModule.kt`:

```kotlin
factoryOf(::<Action><Entity>UseCase)
```

Use `factoryOf` (not `singleOf`) — use cases are stateless and should be new instances.

## 4. Inject into ViewModel

In the ViewModel constructor, add the use case as a parameter:

```kotlin
class <Name>ViewModel(
    private val <action><Entity>UseCase: <Action><Entity>UseCase,
    // ...existing use cases
) : ViewModel() {
```

Koin will automatically resolve it — no additional wiring needed in the feature module.
