package com.example.foodrestaurantdeliveryapp.data.entity.menu


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant

@Entity(
    tableName = "menu_entries",
    foreignKeys = [
        ForeignKey(
            entity = Restaurant::class,
            parentColumns = ["restaurantId"],
            childColumns = ["restaurantId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FoodItem::class,
            parentColumns = ["foodId"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["restaurantId"], unique = false, name = "idx_menu_entries_restaurantId"),
        Index(value = ["foodId"], unique = false, name = "idx_menu_entries_foodId"),
        Index(value = ["restaurantId", "foodId"], unique = true, name = "idx_menu_entries_restaurantId_foodId_unique")
    ]
)
data class MenuEntry(
    @PrimaryKey(autoGenerate = true)
    val menuId: Int = 0,

    val restaurantId: Int,

    val foodId: Int,

    val price: String,

    val isAvailable: Boolean
)