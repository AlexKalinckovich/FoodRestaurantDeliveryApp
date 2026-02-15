package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.repository.RestaurantRepository
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    restaurantRepository: RestaurantRepository
) : ViewModel() {

    private val restaurantId: Int = savedStateHandle.get<Int>("restaurantId") ?: 0

    val uiState: StateFlow<Restaurant?> =
        restaurantRepository.getRestaurantStream(restaurantId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
}