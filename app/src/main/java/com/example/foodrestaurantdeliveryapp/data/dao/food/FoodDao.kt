package com.example.foodrestaurantdeliveryapp.data.dao.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items WHERE foodId = :foodId")
    suspend fun getFoodItem(foodId: Int): FoodItem?

    @Update
    suspend fun updateFoodItem(foodItem: FoodItem)
    @Insert
    suspend fun insert(foodItem: FoodItem): Long

    @Query("DELETE FROM food_items")
    suspend fun deleteAll()
}