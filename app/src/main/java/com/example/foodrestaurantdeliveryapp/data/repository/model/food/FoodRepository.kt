package com.example.foodrestaurantdeliveryapp.data.repository.model.food

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

    suspend fun getFoodItem(foodId: Int): FoodItem? = foodDao.getFoodItem(foodId)

    suspend fun insertFoodItem(foodItem: FoodItem): Long = foodDao.insert(foodItem)

    suspend fun updateFoodItem(foodItem: FoodItem) = foodDao.updateFoodItem(foodItem)

    suspend fun deleteAll() {
        foodDao.deleteAll()
    }
}