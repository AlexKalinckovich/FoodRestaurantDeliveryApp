package com.example.foodrestaurantdeliveryapp.data

import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.model.category.CategoryRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.food.FoodRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.food.foodProduct.FoodProductRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.MenuEntryRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.RestaurantRepository
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
    private val menuEntryRepository: MenuEntryRepository,
    private val foodProductRepository: FoodProductRepository
) {

    fun initialize(scope: CoroutineScope = CoroutineScope(Dispatchers.IO)) {
        scope.launch {
            clearAllData()
            insertInitialData()
        }
    }

    private suspend fun clearAllData() {
        menuEntryRepository.deleteAll()
        foodRepository.deleteAll()
        restaurantRepository.deleteAll()
        categoryRepository.deleteAll()
        foodProductRepository.deleteAll()
    }

    private suspend fun insertInitialData() {
        val categoryMap = insertCategories()
        val restaurantMap = insertRestaurants()
        val foodMap = insertFoodItems(categoryMap)
        insertMenuEntries(restaurantMap, foodMap)
    }

    private suspend fun insertCategories(): Map<String, Long> {
        val categories = listOf(
            "Курица" to Category(name = "Курица"),
            "Пицца" to Category(name = "Пицца"),
            "Бургеры" to Category(name = "Бургеры"),
            "Шаурма" to Category(name = "Шаурма")
        )
        val map = mutableMapOf<String, Long>()
        categories.forEach { (name, category) ->
            map[name] = categoryRepository.insertCategory(category)
        }
        return map
    }

    private suspend fun insertRestaurants(): Map<String, Long> {
        val restaurants = listOf(
            "KFC" to Restaurant(name = "KFC", address = "ул. Ленина, 10", deliveryFee = "100 руб", imageUrl = "https://.../kfc.jpg"),
            "Додо Пицца" to Restaurant(name = "Додо Пицца", address = "пр-т Мира, 5", deliveryFee = "150 руб", imageUrl = "https://.../dodo.jpg"),
            "Бургер Кинг" to Restaurant(name = "Бургер Кинг", address = "ул. Советская, 20", deliveryFee = "120 руб", imageUrl = "https://.../bk.jpg"),
            "Papa-doner" to Restaurant(name = "Papa-doner", address = "ул. Пушкина, 3", deliveryFee = "90 руб", imageUrl = "https://.../doner.jpg")
        )
        val map = mutableMapOf<String, Long>()
        restaurants.forEach { (name, restaurant) ->
            map[name] = restaurantRepository.insertRestaurant(restaurant)
        }
        return map
    }

    private suspend fun insertFoodItems(categoryMap: Map<String, Long>): Map<String, Long> {
        val foodItems = listOf(
            "Острый стрипс" to FoodItem(
                categoryId = categoryMap["Курица"]!!.toInt(),
                name = "Острый стрипс",
                description = "3 кусочка острого куриного филе",
                imageUrl = "https://.../strips.jpg"
            ),
            "Баскет 12 крыльев" to FoodItem(
                categoryId = categoryMap["Курица"]!!.toInt(),
                name = "Баскет 12 крыльев",
                description = "12 куриных крылышек в оригинальной панировке",
                imageUrl = "https://.../wings.jpg"
            ),
            "Твистер" to FoodItem(
                categoryId = categoryMap["Курица"]!!.toInt(),
                name = "Твистер",
                description = "Курица, овощи, соус в мягкой тортилье",
                imageUrl = "https://.../twister.jpg"
            ),
            "Пепперони" to FoodItem(
                categoryId = categoryMap["Пицца"]!!.toInt(),
                name = "Пепперони",
                description = "Пицца с пепперони и моцареллой",
                imageUrl = "https://.../pepperoni.jpg"
            ),
            "Гавайская" to FoodItem(
                categoryId = categoryMap["Пицца"]!!.toInt(),
                name = "Гавайская",
                description = "С курицей и ананасом",
                imageUrl = "https://.../hawaiian.jpg"
            ),
            "Маргарита" to FoodItem(
                categoryId = categoryMap["Пицца"]!!.toInt(),
                name = "Маргарита",
                description = "Классическая пицца с томатами и сыром",
                imageUrl = "https://.../margherita.jpg"
            ),
            "Воппер" to FoodItem(
                categoryId = categoryMap["Бургеры"]!!.toInt(),
                name = "Воппер",
                description = "Большой бургер с говяжьей котлетой",
                imageUrl = "https://.../whopper.jpg"
            ),
            "Чизбургер" to FoodItem(
                categoryId = categoryMap["Бургеры"]!!.toInt(),
                name = "Чизбургер",
                description = "Классический с сыром и котлетой",
                imageUrl = "https://.../cheeseburger.jpg"
            ),
            "Королевская курица" to FoodItem(
                categoryId = categoryMap["Бургеры"]!!.toInt(),
                name = "Королевская курица",
                description = "Сочная куриная котлета, салат, соус",
                imageUrl = "https://.../chicken.jpg"
            ),
            "Донер с курицей" to FoodItem(
                categoryId = categoryMap["Шаурма"]!!.toInt(),
                name = "Донер с курицей",
                description = "Классический донер в лаваше",
                imageUrl = "https://.../doner_ch.jpg"
            ),
            "Донер с говядиной" to FoodItem(
                categoryId = categoryMap["Шаурма"]!!.toInt(),
                name = "Донер с говядиной",
                description = "С говядиной и свежими овощами",
                imageUrl = "https://.../doner_beef.jpg"
            ),
            "Сытный донер" to FoodItem(
                categoryId = categoryMap["Шаурма"]!!.toInt(),
                name = "Сытный донер",
                description = "Двойная порция мяса, больше овощей",
                imageUrl = "https://.../doner_big.jpg"
            )
        )
        val map = mutableMapOf<String, Long>()
        foodItems.forEach { (name, foodItem) ->
            map[name] = foodRepository.insertFoodItem(foodItem)
        }
        return map
    }

    private suspend fun insertMenuEntries(
        restaurantMap: Map<String, Long>,
        foodMap: Map<String, Long>
    ) {
        val menuEntries = listOf(
            MenuEntry(restaurantId = restaurantMap["KFC"]!!.toInt(), foodId = foodMap["Острый стрипс"]!!.toInt(), price = "250 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["KFC"]!!.toInt(), foodId = foodMap["Баскет 12 крыльев"]!!.toInt(), price = "450 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["KFC"]!!.toInt(), foodId = foodMap["Твистер"]!!.toInt(), price = "220 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Додо Пицца"]!!.toInt(), foodId = foodMap["Пепперони"]!!.toInt(), price = "500 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Додо Пицца"]!!.toInt(), foodId = foodMap["Гавайская"]!!.toInt(), price = "550 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Додо Пицца"]!!.toInt(), foodId = foodMap["Маргарита"]!!.toInt(), price = "400 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Бургер Кинг"]!!.toInt(), foodId = foodMap["Воппер"]!!.toInt(), price = "300 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Бургер Кинг"]!!.toInt(), foodId = foodMap["Чизбургер"]!!.toInt(), price = "150 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Бургер Кинг"]!!.toInt(), foodId = foodMap["Королевская курица"]!!.toInt(), price = "250 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Papa-doner"]!!.toInt(), foodId = foodMap["Донер с курицей"]!!.toInt(), price = "200 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Papa-doner"]!!.toInt(), foodId = foodMap["Донер с говядиной"]!!.toInt(), price = "220 руб", isAvailable = true),
            MenuEntry(restaurantId = restaurantMap["Papa-doner"]!!.toInt(), foodId = foodMap["Сытный донер"]!!.toInt(), price = "280 руб", isAvailable = true)
        )
        menuEntries.forEach { menuEntryRepository.insertMenuEntry(it) }
    }
}