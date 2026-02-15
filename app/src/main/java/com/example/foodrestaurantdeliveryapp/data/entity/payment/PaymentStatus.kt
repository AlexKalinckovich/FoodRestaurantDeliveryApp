package com.example.foodrestaurantdeliveryapp.data.entity.payment


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_statuses")
data class PaymentStatus(
    @PrimaryKey(autoGenerate = true) val statusId: Int = 0,
    val name: String
)