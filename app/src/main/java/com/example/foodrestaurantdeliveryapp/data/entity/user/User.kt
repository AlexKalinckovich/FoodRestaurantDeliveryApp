package com.example.foodrestaurantdeliveryapp.data.entity.user

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class User(
    @DocumentId val uid: String = "",
    val email: String = "",
    val role: String = "customer",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "customer", 0L, 0L)
}