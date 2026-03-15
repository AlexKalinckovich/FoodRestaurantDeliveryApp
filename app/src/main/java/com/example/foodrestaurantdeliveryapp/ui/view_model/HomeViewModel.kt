package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.model.category.CategoryRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            restaurantRepository.getAllRestaurants().collect { restaurants ->
                _uiState.update { it.copy(restaurants = restaurants) }
            }
        }
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurantRepository.deleteRestaurant(restaurant)
        }
    }

    fun addSampleRestaurant() {
        // TODO: реализовать добавление примера
    }
}

data class HomeUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null
)