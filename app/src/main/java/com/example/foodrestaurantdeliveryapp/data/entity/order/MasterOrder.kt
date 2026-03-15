package com.example.foodrestaurantdeliveryapp.data.entity.order

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.user.User


data class MasterOrder(
    val masterOrderId: Int = 0,

    val userId: Int,

    val totalAmount: String,

    val createdAt: Long
)