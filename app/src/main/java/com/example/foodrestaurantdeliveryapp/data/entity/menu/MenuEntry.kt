package com.example.foodrestaurantdeliveryapp.data.entity.menu

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class MenuEntry(
    val menuEntryId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val foodId: String = "",
    val foodName: String = "",
    val foodDescription: String = "",
    val foodImageUrl: String = "",
    val price: Double = 0.0,
    val currency: String = "USD",
    val isAvailable: Boolean = false,
    val categoryId: String? = null,
    val category: String? = null,
    val searchTokens: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", "", "", 0.0, "USD", false, null, null, emptyList(), 0L, 0L)
}