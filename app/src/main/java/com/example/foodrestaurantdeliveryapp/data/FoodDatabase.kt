package com.example.foodrestaurantdeliveryapp.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodrestaurantdeliveryapp.data.dao.food.CategoryDao
import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodDao
import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodProductDao
import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.dao.restaurant.RestaurantDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.entity.order.MasterOrder
import com.example.foodrestaurantdeliveryapp.data.entity.order.OrderItem
import com.example.foodrestaurantdeliveryapp.data.entity.order.OrderStatus
import com.example.foodrestaurantdeliveryapp.data.entity.order.RestaurantOrder
import com.example.foodrestaurantdeliveryapp.data.entity.payment.Payment
import com.example.foodrestaurantdeliveryapp.data.entity.payment.PaymentMethod
import com.example.foodrestaurantdeliveryapp.data.entity.payment.PaymentStatus
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.entity.user.User

@Database(
    entities = [
        FoodProduct::class
    ],
    version = 6,
    exportSchema = false
)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodProductDao(): FoodProductDao
    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "food_delivery_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}