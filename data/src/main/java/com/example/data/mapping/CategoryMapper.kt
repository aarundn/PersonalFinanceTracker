package com.example.data.mapping

import com.example.data.local.model.CategoryEntity
import com.example.domain.model.Category

// for future use if we add add a functionality to edit categories

fun CategoryEntity.toCategoryDomain(): Category {
    return Category(
        id = id,
        name = name,
    )
}

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,

)

fun List<CategoryEntity>.toCategoryDomain(): List<Category> = map { it.toCategoryDomain()}