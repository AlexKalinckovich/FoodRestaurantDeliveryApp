package com.example.foodrestaurantdeliveryapp.data.entity.order

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant

@Entity(
    tableName = "restaurant_orders",
    foreignKeys = [
        ForeignKey(
            entity = MasterOrder::class,
            parentColumns = ["masterOrderId"],
            childColumns = ["masterOrderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Restaurant::class,
            parentColumns = ["restaurantId"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = OrderStatus::class,
            parentColumns = ["statusId"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class RestaurantOrder(
    @PrimaryKey(autoGenerate = true)
    val subOrderId: Int = 0,

    val masterOrderId: Int,

    val restaurantId: Int,

    val statusId: Int
)