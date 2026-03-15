package com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model

import androidx.room.Embedded
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry

data class MenuEntryWithFood(
    val menuEntry: MenuEntry,
    val foodItem: FoodItem
)