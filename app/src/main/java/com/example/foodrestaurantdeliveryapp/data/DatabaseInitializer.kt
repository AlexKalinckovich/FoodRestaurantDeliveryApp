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
import com.example.foodrestaurantdeliveryapp.utils.SearchTokenGenerator
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
        insertMenuEntries(restaurantMap, foodMap, categoryMap)
    }

    private suspend fun insertCategories(): Map<String, String> {
        val categories = listOf(
            "Курица" to Category(name = "Курица"),
            "Пицца" to Category(name = "Пицца"),
            "Бургеры" to Category(name = "Бургеры"),
            "Шаурма" to Category(name = "Шаурма")
        )
        val map = mutableMapOf<String, String>()
        categories.forEach { (name, category) ->
            map[name] = categoryRepository.insertCategory(category)
        }
        return map
    }

    private suspend fun insertRestaurants(): Map<String, String> {
        val restaurants = listOf(
            "KFC" to Restaurant(
                name = "KFC",
                address = "ул. Ленина, 10",
                deliveryFee = 100.0,
                imageUrl = "https://.../kfc.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("KFC") + SearchTokenGenerator.generateTokens("ул. Ленина, 10")
            ),
            "Додо Пицца" to Restaurant(
                name = "Додо Пицца",
                address = "пр-т Мира, 5",
                deliveryFee = 150.0,
                imageUrl = "https://.../dodo.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Додо Пицца") + SearchTokenGenerator.generateTokens("пр-т Мира, 5")
            ),
            "Бургер Кинг" to Restaurant(
                name = "Бургер Кинг",
                address = "ул. Советская, 20",
                deliveryFee = 120.0,
                imageUrl = "https://.../bk.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Бургер Кинг") + SearchTokenGenerator.generateTokens("ул. Советская, 20")
            ),
            "Papa-doner" to Restaurant(
                name = "Papa-doner",
                address = "ул. Пушкина, 3",
                deliveryFee = 90.0,
                imageUrl = "https://.../doner.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Papa-doner") + SearchTokenGenerator.generateTokens("ул. Пушкина, 3")
            )
        )
        val map = mutableMapOf<String, String>()
        restaurants.forEach { (name, restaurant) ->
            map[name] = restaurantRepository.insertRestaurant(restaurant)
        }
        return map
    }

    private suspend fun insertFoodItems(categoryMap: Map<String, String>): Map<String, String> {
        val foodItems = listOf(
            "Острый стрипс" to FoodItem(
                categoryId = categoryMap["Курица"]!!,
                categoryName = "Курица",
                name = "Острый стрипс",
                description = "3 кусочка острого куриного филе",
                imageUrl = "https://.../strips.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Острый стрипс") + SearchTokenGenerator.generateTokens("3 кусочка острого куриного филе")
            ),
            "Баскет 12 крыльев" to FoodItem(
                categoryId = categoryMap["Курица"]!!,
                categoryName = "Курица",
                name = "Баскет 12 крыльев",
                description = "12 куриных крылышек в оригинальной панировке",
                imageUrl = "https://.../wings.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Баскет 12 крыльев") + SearchTokenGenerator.generateTokens("12 куриных крылышек в оригинальной панировке")
            ),
            "Твистер" to FoodItem(
                categoryId = categoryMap["Курица"]!!,
                categoryName = "Курица",
                name = "Твистер",
                description = "Курица, овощи, соус в мягкой тортилье",
                imageUrl = "https://.../twister.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Твистер") + SearchTokenGenerator.generateTokens("Курица, овощи, соус в мягкой тортилье")
            ),
            "Пепперони" to FoodItem(
                categoryId = categoryMap["Пицца"]!!,
                categoryName = "Пицца",
                name = "Пепперони",
                description = "Пицца с пепперони и моцареллой",
                imageUrl = "https://.../pepperoni.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Пепперони") + SearchTokenGenerator.generateTokens("Пицца с пепперони и моцареллой")
            ),
            "Гавайская" to FoodItem(
                categoryId = categoryMap["Пицца"]!!,
                categoryName = "Пицца",
                name = "Гавайская",
                description = "С курицей и ананасом",
                imageUrl = "https://.../hawaiian.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Гавайская") + SearchTokenGenerator.generateTokens("С курицей и ананасом")
            ),
            "Маргарита" to FoodItem(
                categoryId = categoryMap["Пицца"]!!,
                categoryName = "Пицца",
                name = "Маргарита",
                description = "Классическая пицца с томатами и сыром",
                imageUrl = "https://.../margherita.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Маргарита") + SearchTokenGenerator.generateTokens("Классическая пицца с томатами и сыром")
            ),
            "Воппер" to FoodItem(
                categoryId = categoryMap["Бургеры"]!!,
                categoryName = "Бургеры",
                name = "Воппер",
                description = "Большой бургер с говяжьей котлетой",
                imageUrl = "https://.../whopper.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Воппер") + SearchTokenGenerator.generateTokens("Большой бургер с говяжьей котлетой")
            ),
            "Чизбургер" to FoodItem(
                categoryId = categoryMap["Бургеры"]!!,
                categoryName = "Бургеры",
                name = "Чизбургер",
                description = "Классический с сыром и котлетой",
                imageUrl = "https://.../cheeseburger.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Чизбургер") + SearchTokenGenerator.generateTokens("Классический с сыром и котлетой")
            ),
            "Королевская курица" to FoodItem(
                categoryId = categoryMap["Бургеры"]!!,
                categoryName = "Бургеры",
                name = "Королевская курица",
                description = "Сочная куриная котлета, салат, соус",
                imageUrl = "https://.../chicken.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Королевская курица") + SearchTokenGenerator.generateTokens("Сочная куриная котлета, салат, соус")
            ),
            "Донер с курицей" to FoodItem(
                categoryId = categoryMap["Шаурма"]!!,
                categoryName = "Шаурма",
                name = "Донер с курицей",
                description = "Классический донер в лаваше",
                imageUrl = "https://.../doner_ch.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Донер с курицей") + SearchTokenGenerator.generateTokens("Классический донер в лаваше")
            ),
            "Донер с говядиной" to FoodItem(
                categoryId = categoryMap["Шаурма"]!!,
                categoryName = "Шаурма",
                name = "Донер с говядиной",
                description = "С говядиной и свежими овощами",
                imageUrl = "https://.../doner_beef.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Донер с говядиной") + SearchTokenGenerator.generateTokens("С говядиной и свежими овощами")
            ),
            "Сытный донер" to FoodItem(
                categoryId = categoryMap["Шаурма"]!!,
                categoryName = "Шаурма",
                name = "Сытный донер",
                description = "Двойная порция мяса, больше овощей",
                imageUrl = "https://.../doner_big.jpg",
                searchTokens = SearchTokenGenerator.generateTokens("Сытный донер") + SearchTokenGenerator.generateTokens("Двойная порция мяса, больше овощей")
            )
        )
        val map = mutableMapOf<String, String>()
        foodItems.forEach { (name, foodItem) ->
            map[name] = foodRepository.insertFoodItem(foodItem)
        }
        return map
    }

    private suspend fun insertMenuEntries(
        restaurantMap: Map<String, String>,
        foodMap: Map<String, String>,
        categoryMap: Map<String, String>
    ) {
        val menuEntries = listOf(
            MenuEntry(
                restaurantId = restaurantMap["KFC"]!!,
                restaurantName = "KFC",
                foodId = foodMap["Острый стрипс"]!!,
                foodName = "Острый стрипс",
                foodDescription = "3 кусочка острого куриного филе",
                foodImageUrl = "https://.../strips.jpg",
                price = 250.0,
                isAvailable = true,
                categoryId = categoryMap["Курица"],
                category = "Курица",
                searchTokens = SearchTokenGenerator.generateTokens("Острый стрипс")
            ),
            MenuEntry(
                restaurantId = restaurantMap["KFC"]!!,
                restaurantName = "KFC",
                foodId = foodMap["Баскет 12 крыльев"]!!,
                foodName = "Баскет 12 крыльев",
                foodDescription = "12 куриных крылышек в оригинальной панировке",
                foodImageUrl = "https://.../wings.jpg",
                price = 450.0,
                isAvailable = true,
                categoryId = categoryMap["Курица"],
                category = "Курица",
                searchTokens = SearchTokenGenerator.generateTokens("Баскет 12 крыльев")
            ),
            MenuEntry(
                restaurantId = restaurantMap["KFC"]!!,
                restaurantName = "KFC",
                foodId = foodMap["Твистер"]!!,
                foodName = "Твистер",
                foodDescription = "Курица, овощи, соус в мягкой тортилье",
                foodImageUrl = "https://.../twister.jpg",
                price = 220.0,
                isAvailable = true,
                categoryId = categoryMap["Курица"],
                category = "Курица",
                searchTokens = SearchTokenGenerator.generateTokens("Твистер")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Додо Пицца"]!!,
                restaurantName = "Додо Пицца",
                foodId = foodMap["Пепперони"]!!,
                foodName = "Пепперони",
                foodDescription = "Пицца с пепперони и моцареллой",
                foodImageUrl = "https://.../pepperoni.jpg",
                price = 500.0,
                isAvailable = true,
                categoryId = categoryMap["Пицца"],
                category = "Пицца",
                searchTokens = SearchTokenGenerator.generateTokens("Пепперони")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Додо Пицца"]!!,
                restaurantName = "Додо Пицца",
                foodId = foodMap["Гавайская"]!!,
                foodName = "Гавайская",
                foodDescription = "С курицей и ананасом",
                foodImageUrl = "https://.../hawaiian.jpg",
                price = 550.0,
                isAvailable = true,
                categoryId = categoryMap["Пицца"],
                category = "Пицца",
                searchTokens = SearchTokenGenerator.generateTokens("Гавайская")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Додо Пицца"]!!,
                restaurantName = "Додо Пицца",
                foodId = foodMap["Маргарита"]!!,
                foodName = "Маргарита",
                foodDescription = "Классическая пицца с томатами и сыром",
                foodImageUrl = "https://.../margherita.jpg",
                price = 400.0,
                isAvailable = true,
                categoryId = categoryMap["Пицца"],
                category = "Пицца",
                searchTokens = SearchTokenGenerator.generateTokens("Маргарита")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Бургер Кинг"]!!,
                restaurantName = "Бургер Кинг",
                foodId = foodMap["Воппер"]!!,
                foodName = "Воппер",
                foodDescription = "Большой бургер с говяжьей котлетой",
                foodImageUrl = "https://.../whopper.jpg",
                price = 300.0,
                isAvailable = true,
                categoryId = categoryMap["Бургеры"],
                category = "Бургеры",
                searchTokens = SearchTokenGenerator.generateTokens("Воппер")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Бургер Кинг"]!!,
                restaurantName = "Бургер Кинг",
                foodId = foodMap["Чизбургер"]!!,
                foodName = "Чизбургер",
                foodDescription = "Классический с сыром и котлетой",
                foodImageUrl = "https://.../cheeseburger.jpg",
                price = 150.0,
                isAvailable = true,
                categoryId = categoryMap["Бургеры"],
                category = "Бургеры",
                searchTokens = SearchTokenGenerator.generateTokens("Чизбургер")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Бургер Кинг"]!!,
                restaurantName = "Бургер Кинг",
                foodId = foodMap["Королевская курица"]!!,
                foodName = "Королевская курица",
                foodDescription = "Сочная куриная котлета, салат, соус",
                foodImageUrl = "https://.../chicken.jpg",
                price = 250.0,
                isAvailable = true,
                categoryId = categoryMap["Бургеры"],
                category = "Бургеры",
                searchTokens = SearchTokenGenerator.generateTokens("Королевская курица")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Papa-doner"]!!,
                restaurantName = "Papa-doner",
                foodId = foodMap["Донер с курицей"]!!,
                foodName = "Донер с курицей",
                foodDescription = "Классический донер в лаваше",
                foodImageUrl = "https://.../doner_ch.jpg",
                price = 200.0,
                isAvailable = true,
                categoryId = categoryMap["Шаурма"],
                category = "Шаурма",
                searchTokens = SearchTokenGenerator.generateTokens("Донер с курицей")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Papa-doner"]!!,
                restaurantName = "Papa-doner",
                foodId = foodMap["Донер с говядиной"]!!,
                foodName = "Донер с говядиной",
                foodDescription = "С говядиной и свежими овощами",
                foodImageUrl = "https://.../doner_beef.jpg",
                price = 220.0,
                isAvailable = true,
                categoryId = categoryMap["Шаурма"],
                category = "Шаурма",
                searchTokens = SearchTokenGenerator.generateTokens("Донер с говядиной")
            ),
            MenuEntry(
                restaurantId = restaurantMap["Papa-doner"]!!,
                restaurantName = "Papa-doner",
                foodId = foodMap["Сытный донер"]!!,
                foodName = "Сытный донер",
                foodDescription = "Двойная порция мяса, больше овощей",
                foodImageUrl = "https://.../doner_big.jpg",
                price = 280.0,
                isAvailable = true,
                categoryId = categoryMap["Шаурма"],
                category = "Шаурма",
                searchTokens = SearchTokenGenerator.generateTokens("Сытный донер")
            )
        )
        menuEntries.forEach { menuEntryRepository.insertMenuEntry(it) }
    }
}