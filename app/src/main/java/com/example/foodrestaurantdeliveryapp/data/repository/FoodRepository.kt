package com.example.foodrestaurantdeliveryapp.data.repository


import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    suspend fun insertFoodItems(vararg foodItems: FoodItem) {
        foodItems.forEach { foodDao.insert(foodItem = it) }
    }
}