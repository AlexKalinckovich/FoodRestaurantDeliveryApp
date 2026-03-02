package com.example.foodrestaurantdeliveryapp.data.dao.restaurant

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants ORDER BY restaurantId DESC")
    fun getAllRestaurants(): Flow<List<Restaurant>>

    @Query("SELECT * FROM restaurants WHERE restaurantId = :id")
    fun getRestaurantById(id: Int): Flow<Restaurant>

    @Insert(onConflict = REPLACE)
    suspend fun insert(restaurant: Restaurant) : Long

    @Update
    suspend fun update(restaurant: Restaurant)

    @Delete
    suspend fun delete(restaurant: Restaurant)

    @Query("DELETE FROM restaurants")
    suspend fun deleteAll()
}