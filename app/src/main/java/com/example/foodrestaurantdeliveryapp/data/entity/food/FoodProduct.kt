package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_products")
data class FoodProduct(
    @PrimaryKey
    val barcode: String,
    val name: String,
    val brand: String?,
    val calories: Double?,
    val ingredients: String?,
    val imageUrl: String?,
    val lastUpdated: Long
)