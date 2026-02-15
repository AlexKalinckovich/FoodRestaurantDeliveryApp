package com.example.foodrestaurantdeliveryapp.data.entity.order

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.user.User

@Entity(
    tableName = "master_orders",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MasterOrder(
    @PrimaryKey(autoGenerate = true)
    val masterOrderId: Int = 0,

    val userId: Int,

    val totalAmount: String,

    val createdAt: Long
)