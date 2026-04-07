package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.DatabaseInitializer
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.auth.AuthRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.category.CategoryRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.RestaurantRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.fuzzySearch
import com.example.foodrestaurantdeliveryapp.ui.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SEARCH_DELAY: Long = 300L
private const val CLIENT_SITE_SEARCH_THRESHOLD: Int = 3

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    private val categoryRepository: CategoryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    private var searchJob: Job? = null
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var allRestaurants: List<Restaurant> = DatabaseInitializer.insertedRestaurants

    init {
        viewModelScope.launch {
            restaurantRepository.getAllRestaurants().collect { restaurants ->
                if(restaurants.isNotEmpty()) {
                    allRestaurants = restaurants
                }
                updateDisplayedRestaurants()
            }
        }
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _uiState.update {
                    it.copy(
                        userEmail = user?.email,
                        isSignedIn = user != null
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }

    fun updateSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(timeMillis = SEARCH_DELAY)
            _uiState.update { it.copy(searchQuery = query) }
            updateDisplayedRestaurants()
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
        viewModelScope.launch { updateDisplayedRestaurants() }
    }

    fun updateSortOption(option: SortOption) {
        _uiState.update { it.copy(sortOption = option) }
        viewModelScope.launch { updateDisplayedRestaurants() }
    }

    private suspend fun updateDisplayedRestaurants() {
        val state = _uiState.value
        var filtered = allRestaurants

        val searchQuery: String = state.searchQuery
        if (searchQuery.isNotBlank()) {
            filtered = if (searchQuery.length < CLIENT_SITE_SEARCH_THRESHOLD) {
                filtered.fuzzySearch(state.searchQuery)
            } else {
                restaurantRepository.searchRestaurantsFuzzy(searchQuery, allRestaurants = filtered)
            }
        }

        filtered = when (state.sortOption) {
            SortOption.NAME_ASC -> filtered.sortedBy { it.name.lowercase() }
            SortOption.NAME_DESC -> filtered.sortedByDescending { it.name.lowercase() }
            SortOption.DELIVERY_FEE_ASC -> filtered.sortedBy { it.deliveryFee }
            SortOption.DELIVERY_FEE_DESC -> filtered.sortedByDescending { it.deliveryFee }
        }

        _uiState.update {
            it.copy(
                displayedRestaurants = filtered,
                isSearching = searchQuery.isNotBlank()
            )
        }
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurantRepository.deleteRestaurant(restaurant)
        }
    }

    fun addSampleRestaurant() {
        viewModelScope.launch {
            restaurantRepository.insertRestaurant(
                name = "Sample Restaurant",
                address = "Sample Address",
                deliveryFee = 99.0,
                imageUrl = "https://ik.imagekit.io/pb0e83twy8/smile.png"
            )
        }
    }
}

data class HomeUiState(
    val displayedRestaurants: List<Restaurant> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NAME_ASC,
    val isSearching: Boolean = false,
    val isLoading: Boolean = true,
    val userEmail: String? = null,
    val isSignedIn: Boolean = false
)