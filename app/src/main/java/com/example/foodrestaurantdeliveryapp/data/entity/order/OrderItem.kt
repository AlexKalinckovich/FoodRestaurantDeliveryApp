package com.example.foodrestaurantdeliveryapp.data.entity.order


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry


data class OrderItem(
    val subOrderId: Int,
    val menuEntryId: Int,
    val quantity: Int,
    val priceAtPurchase: String
)