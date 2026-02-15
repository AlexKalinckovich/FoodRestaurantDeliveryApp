package com.example.foodrestaurantdeliveryapp.data.dao.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_items")
    fun getAllFood(): Flow<List<FoodItem>>

    @Insert
    suspend fun insert(foodItem: FoodItem)
}