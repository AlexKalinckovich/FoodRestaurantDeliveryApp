package com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model

data class MenuWithDetails(
    val menuId: String,

    val name: String,

    val description: String,

    val imageUrl: String,

    val price: Double,

    val isAvailable: Boolean
)