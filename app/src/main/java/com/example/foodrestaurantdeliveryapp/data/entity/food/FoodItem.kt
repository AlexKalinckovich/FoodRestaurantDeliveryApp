package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class FoodItem(
    val foodId: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val name: String = "",
    val nameLowercase: String = name.lowercase(),
    val description: String = "",
    val descriptionLowercase: String = description.lowercase(),
    val imageUrl: String = "",
    val searchTokens: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", "", "", "", emptyList(), 0L, 0L)
}