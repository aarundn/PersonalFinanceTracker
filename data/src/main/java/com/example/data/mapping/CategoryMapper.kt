package com.example.data.mapping

import com.example.data.local.model.CategoryEntity
import com.example.domain.model.Category


fun CategoryEntity.toCategoryDomain(): Category {
    return Category(
        id = id,
        name = name,
        type = type,
        icon = icon,
        color = color
    )
}

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    type = type,
    icon = icon,
    color = color
)

fun List<CategoryEntity>.toCategoryDomain(): List<Category> = map { it.toCategoryDomain()}