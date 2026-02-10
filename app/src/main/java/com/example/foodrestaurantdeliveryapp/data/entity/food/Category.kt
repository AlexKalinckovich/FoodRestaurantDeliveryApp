package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Int = 0,

    val name: String
)