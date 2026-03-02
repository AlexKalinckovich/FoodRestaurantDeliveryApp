package com.example.foodrestaurantdeliveryapp.data.dao.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY categoryId DESC")
    fun getAllCategories() : Flow<List<Category>>;

    @Insert
    suspend fun insert(category: Category) : Long

    @Query("SELECT COUNT(categoryId) FROM categories")
    suspend fun getCount() : Int

    @Query("DELETE FROM categories")
    suspend fun deleteAll()
}