package com.example.foodrestaurantdeliveryapp.data.entity.restaurant

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class Restaurant(
    val restaurantId: String = "",
    val name: String = "",
    val nameLowercase: String = name.lowercase(),
    val address: String = "",
    val addressLowercase: String = address.lowercase(),
    val deliveryFee: Double = 0.0,
    val imageUrl: String = "",
    val searchTokens: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", 0.0, "", emptyList(), 0L, 0L)
}