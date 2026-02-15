package com.example.foodrestaurantdeliveryapp.data.entity.order


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = RestaurantOrder::class,
            parentColumns = ["subOrderId"],
            childColumns = ["subOrderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MenuEntry::class,
            parentColumns = ["menuId"],
            childColumns = ["menuEntryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true) val orderItemId: Int = 0,
    val subOrderId: Int,
    val menuEntryId: Int,
    val quantity: Int,
    val priceAtPurchase: String
)