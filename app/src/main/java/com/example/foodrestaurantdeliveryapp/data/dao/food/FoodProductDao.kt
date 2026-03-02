package com.example.foodrestaurantdeliveryapp.data.dao.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct

@Dao
interface FoodProductDao {
    @Query("SELECT * FROM food_products WHERE barcode = :barcode")
    suspend fun getProduct(barcode: String): FoodProduct?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: FoodProduct)

    @Query("DELETE FROM food_products WHERE lastUpdated < :cutoffTime")
    suspend fun deleteOldEntries(cutoffTime: Long)
}