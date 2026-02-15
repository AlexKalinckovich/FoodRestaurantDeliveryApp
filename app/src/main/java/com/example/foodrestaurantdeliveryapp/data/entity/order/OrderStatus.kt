package com.example.foodrestaurantdeliveryapp.data.entity.order

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_statuses")
data class OrderStatus(
    @PrimaryKey(autoGenerate = true)
    val statusId: Int = 0,

    val name: String
)