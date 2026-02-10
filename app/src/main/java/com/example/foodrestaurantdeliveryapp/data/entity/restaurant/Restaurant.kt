package com.example.foodrestaurantdeliveryapp.data.entity.restaurant

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "restaurants")
data class Restaurant(

    @PrimaryKey(autoGenerate = true)
    val restaurantId: Int = 0,

    val name: String,

    val address: String,

    val deliveryFee: String,

    val imageUrl: String
)