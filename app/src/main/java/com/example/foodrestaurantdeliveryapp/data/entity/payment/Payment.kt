package com.example.foodrestaurantdeliveryapp.data.entity.payment

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.order.MasterOrder
import com.example.foodrestaurantdeliveryapp.data.entity.user.User

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = MasterOrder::class,
            parentColumns = ["masterOrderId"],
            childColumns = ["masterOrderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["payerId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = PaymentMethod::class,
            parentColumns = ["methodId"],
            childColumns = ["methodId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = PaymentStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val paymentId: Int = 0,

    val masterOrderId: Int,

    val payerId: Int?,

    val methodId: Int,

    val statusId: Int,

    val amount: String,

    val transactionDate: Long
)