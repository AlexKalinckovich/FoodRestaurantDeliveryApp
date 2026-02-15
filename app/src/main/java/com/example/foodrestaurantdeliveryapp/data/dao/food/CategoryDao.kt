package com.example.foodrestaurantdeliveryapp.data.dao.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY categoryId DESC")
    fun getAllCategories() : Flow<List<Category>>;

    @Insert
    suspend fun insert(category: Category)

    @Query("SELECT COUNT(categoryId) FROM categories")
    suspend fun getCount() : Int
}