package com.example.foodrestaurantdeliveryapp.data


import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.*
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
class DatabaseInitializer @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val restaurantRepository: RestaurantRepository,
    private val foodRepository: FoodRepository,
    private val menuEntryRepository: MenuEntryRepository
) {

    fun initialize(scope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
        scope.launch {
            if (categoryRepository.isEmpty()) {
                insertInitialData()
            }
        }
    }

    private suspend fun insertInitialData() {
        insertInitialCategories()

        insertInitialRestaurants()

        insertInitialFoodItems()

        insertInitialMenuEntries()
    }

    private suspend fun insertInitialCategories(){
        val categories = listOf(
            Category(name = "Курица"),
            Category(name = "Пицца"),
            Category(name = "Бургеры"),
            Category(name = "Шаурма")
        )
        categoryRepository.insertCategories(*categories.toTypedArray())
    }

    private suspend fun insertInitialRestaurants(){
        val restaurants = listOf(
            Restaurant(name = "KFC", address = "ул. Ленина, 10", deliveryFee = "100 руб", imageUrl = "https://.../kfc.jpg"),
            Restaurant(name = "Додо Пицца", address = "пр-т Мира, 5", deliveryFee = "150 руб", imageUrl = "https://.../dodo.jpg"),
            Restaurant(name = "Бургер Кинг", address = "ул. Советская, 20", deliveryFee = "120 руб", imageUrl = "https://.../bk.jpg"),
            Restaurant(name = "Papa-doner", address = "ул. Пушкина, 3", deliveryFee = "90 руб", imageUrl = "https://.../doner.jpg")
        )
        restaurants.forEach { restaurantRepository.insertRestaurant(it) }
    }

    private suspend fun insertInitialFoodItems(){
        val foodItems = listOf(
            FoodItem(categoryId = 1, name = "Острый стрипс", description = "3 кусочка острого куриного филе", imageUrl = "https://.../strips.jpg"),
            FoodItem(categoryId = 1, name = "Баскет 12 крыльев", description = "12 куриных крылышек в оригинальной панировке", imageUrl = "https://.../wings.jpg"),
            FoodItem(categoryId = 1, name = "Твистер", description = "Курица, овощи, соус в мягкой тортилье", imageUrl = "https://.../twister.jpg"),
            FoodItem(categoryId = 2, name = "Пепперони", description = "Пицца с пепперони и моцареллой", imageUrl = "https://.../pepperoni.jpg"),
            FoodItem(categoryId = 2, name = "Гавайская", description = "С курицей и ананасом", imageUrl = "https://.../hawaiian.jpg"),
            FoodItem(categoryId = 2, name = "Маргарита", description = "Классическая пицца с томатами и сыром", imageUrl = "https://.../margherita.jpg"),
            FoodItem(categoryId = 3, name = "Воппер", description = "Большой бургер с говяжьей котлетой", imageUrl = "https://.../whopper.jpg"),
            FoodItem(categoryId = 3, name = "Чизбургер", description = "Классический с сыром и котлетой", imageUrl = "https://.../cheeseburger.jpg"),
            FoodItem(categoryId = 3, name = "Королевская курица", description = "Сочная куриная котлета, салат, соус", imageUrl = "https://.../chicken.jpg"),
            FoodItem(categoryId = 4, name = "Донер с курицей", description = "Классический донер в лаваше", imageUrl = "https://.../doner_ch.jpg"),
            FoodItem(categoryId = 4, name = "Донер с говядиной", description = "С говядиной и свежими овощами", imageUrl = "https://.../doner_beef.jpg"),
            FoodItem(categoryId = 4, name = "Сытный донер", description = "Двойная порция мяса, больше овощей", imageUrl = "https://.../doner_big.jpg")
        )
        foodRepository.insertFoodItems(*foodItems.toTypedArray())
    }

    private suspend fun insertInitialMenuEntries(){
        val menuEntries = listOf(
            MenuEntry(restaurantId = 1, foodId = 1, price = "250 руб", isAvailable = true),
            MenuEntry(restaurantId = 1, foodId = 2, price = "450 руб", isAvailable = true),
            MenuEntry(restaurantId = 1, foodId = 3, price = "220 руб", isAvailable = true),
            MenuEntry(restaurantId = 2, foodId = 4, price = "500 руб", isAvailable = true),
            MenuEntry(restaurantId = 2, foodId = 5, price = "550 руб", isAvailable = true),
            MenuEntry(restaurantId = 2, foodId = 6, price = "400 руб", isAvailable = true),
            MenuEntry(restaurantId = 3, foodId = 7, price = "300 руб", isAvailable = true),
            MenuEntry(restaurantId = 3, foodId = 8, price = "150 руб", isAvailable = true),
            MenuEntry(restaurantId = 3, foodId = 9, price = "250 руб", isAvailable = true),
            MenuEntry(restaurantId = 4, foodId = 10, price = "200 руб", isAvailable = true),
            MenuEntry(restaurantId = 4, foodId = 11, price = "220 руб", isAvailable = true),
            MenuEntry(restaurantId = 4, foodId = 12, price = "280 руб", isAvailable = true)
        )

        menuEntryRepository.insertMenuEntries(*menuEntries.toTypedArray())
    }
}