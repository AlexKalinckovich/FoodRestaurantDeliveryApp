package com.example.foodrestaurantdeliveryapp.data.dao.menu


import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuEntryWithFood
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import kotlinx.coroutines.flow.Flow





@Dao
interface MenuEntryDao {

    @Query("""
        SELECT 
            m.menuId,
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

    @Query("""
    SELECT 
        m.*,
        f.foodId AS food_foodId,
        f.categoryId AS food_categoryId,
        f.name AS food_name,
        f.description AS food_description,
        f.imageUrl AS food_imageUrl
    FROM menu_entries m
    INNER JOIN food_items f ON m.foodId = f.foodId
    WHERE m.menuId = :menuId
""")
    suspend fun getMenuEntryWithFood(menuId: Int): MenuEntryWithFood?

    @Query("SELECT * FROM menu_entries WHERE menuId = :menuId")
    suspend fun getMenuEntry(menuId: Int): MenuEntry?

    @Update
    suspend fun updateMenuEntry(menuEntry: MenuEntry)

    @Insert
    suspend fun insertMenuEntry(menuEntry: MenuEntry): Long

    @Query("DELETE FROM menu_entries")
    suspend fun deleteAll()
}