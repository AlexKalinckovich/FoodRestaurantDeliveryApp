package com.example.foodrestaurantdeliveryapp.data.entity.payment


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_methods")
data class PaymentMethod(
    @PrimaryKey(autoGenerate = true)
    val methodId: Int = 0,

    val name: String
)