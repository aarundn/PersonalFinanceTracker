package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String?,
    val type: String,
    val color: String?
)
