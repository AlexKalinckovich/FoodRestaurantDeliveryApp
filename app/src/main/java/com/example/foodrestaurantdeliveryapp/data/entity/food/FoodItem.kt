package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "food_items",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val foodId: Int = 0,

    val categoryId: Int,

    val name: String,

    val description: String,

    val imageUrl: String
)