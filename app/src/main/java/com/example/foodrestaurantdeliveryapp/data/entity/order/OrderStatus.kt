package com.example.foodrestaurantdeliveryapp.data.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

data class OrderStatus(
    val statusId: Int = 0,

    val name: String
)