package com.example.foodrestaurantdeliveryapp.data.entity.order

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant


data class RestaurantOrder(
    val subOrderId: Int = 0,

    val masterOrderId: Int,

    val restaurantId: Int,

    val statusId: Int
)