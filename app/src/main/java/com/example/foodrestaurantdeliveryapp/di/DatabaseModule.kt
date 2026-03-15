package com.example.foodrestaurantdeliveryapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodrestaurantdeliveryapp.data.FoodDatabase
import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFoodDatabase(
        @ApplicationContext context: Context
    ): FoodDatabase {
        return Room.databaseBuilder(
            context,
            FoodDatabase::class.java,
            "food_database"
        ).fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodProductDao(database: FoodDatabase): FoodProductDao {
        return database.foodProductDao()
    }
}