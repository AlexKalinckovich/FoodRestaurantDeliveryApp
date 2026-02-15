package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [
        Index(value = ["name"], unique = true, name = "idx_categories_name_unique")
    ]
)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,

    val name: String
)