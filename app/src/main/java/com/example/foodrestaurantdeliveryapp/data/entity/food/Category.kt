package com.example.foodrestaurantdeliveryapp.data.entity.food

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Category(
    val categoryId: String = "",
    val name: String = "",
    val nameLowercase: String = name.lowercase(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // Explicit no-arg constructor for Firestore
    constructor() : this("", "", "", 0L, 0L)
}