package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [
        Index(value = ["categoryId"], unique = false, name = "idx_food_items_categoryId"),
        Index(value = ["name"], unique = false, name = "idx_food_items_name")
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