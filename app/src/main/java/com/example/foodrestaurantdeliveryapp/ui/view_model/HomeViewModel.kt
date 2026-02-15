package com.example.foodrestaurantdeliveryapp.ui.view_model


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.repository.RestaurantRepository
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        restaurantRepository.getAllRestaurants()
            .map { restaurants -> HomeUiState(restaurantList = restaurants) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeUiState()
            )

    fun addSampleRestaurant() {
        viewModelScope.launch {
            restaurantRepository.insertRestaurant(
                Restaurant(
                    name = "New Place",
                    address = "Address",
                    deliveryFee = "5.0",
                    restaurantId = 1,
                    imageUrl = ""
                )
            )
        }
    }

    fun deleteRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            restaurantRepository.deleteRestaurant(restaurant)
        }
    }
}

data class HomeUiState(
    val restaurantList: List<Restaurant> = emptyList()
)