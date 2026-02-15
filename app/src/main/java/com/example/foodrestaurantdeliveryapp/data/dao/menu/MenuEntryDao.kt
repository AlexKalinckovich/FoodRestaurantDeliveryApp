package com.example.foodrestaurantdeliveryapp.data.dao.menu


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import kotlinx.coroutines.flow.Flow


data class MenuWithDetails(
    val name: String,        

    val description: String,

    val imageUrl: String,

    val price: String,

    val isAvailable: Boolean
)

@Dao
interface MenuEntryDao {
    
    @Query("""
        SELECT 
            f.name, 
            f.description, 
            f.imageUrl, 
            m.price, 
            m.isAvailable 
        FROM menu_entries m
        INNER JOIN food_items f ON m.foodId = f.foodId
        WHERE m.restaurantId = :restaurantId
    """)
    fun getMenuForRestaurant(restaurantId: Int): Flow<List<MenuWithDetails>>

    @Insert
    suspend fun insertAll(vararg menuEntries: MenuEntry)

}