package com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model

data class MenuWithDetails(
    val menuId: Int,

    val name: String,

    val description: String,

    val imageUrl: String,

    val price: String,

    val isAvailable: Boolean
)