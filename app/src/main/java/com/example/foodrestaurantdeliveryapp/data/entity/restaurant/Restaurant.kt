package com.example.foodrestaurantdeliveryapp.data.entity.restaurant

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "restaurants",
    indices = [
        Index(value = ["name"], unique = true, name = "idx_restaurants_name"),
        Index(value = ["address"], unique = false, name = "idx_restaurants_address"),
        Index(value = ["deliveryFee"], unique = false, name = "idx_restaurants_delivery_fee")
    ]
)
data class Restaurant(

    @PrimaryKey(autoGenerate = true)
    val restaurantId: Int = 0,

    val name: String,

    val address: String,

    val deliveryFee: String,

    val imageUrl: String
)