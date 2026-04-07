package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val restaurantId: String? = savedStateHandle["restaurantId"]
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    private var dataLoadJob: Job? = null

    init {
        loadData()
    }

    private fun loadData() {
        if (!restaurantId.isNullOrBlank()) {
            dataLoadJob = viewModelScope.launch {
                try {
                    restaurantRepository.waitForInitialization(timeout = 5000L)
                } catch (e: Exception) {
                }

                launch {
                    restaurantRepository.getRestaurantById(restaurantId).collect { restaurant ->
                        _uiState.update {
                            it.copy(
                                restaurant = restaurant,
                                isLoading = restaurant == null
                            )
                        }
                    }
                }

                launch {
                    restaurantRepository.getMenuForRestaurant(restaurantId).collect { menu ->
                        _uiState.update {
                            it.copy(
                                menuItems = menu,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun refreshData() {
        dataLoadJob?.cancel()
        _uiState.update { it.copy(isLoading = true) }
        loadData()
    }

    override fun onCleared() {
        super.onCleared()
        dataLoadJob?.cancel()
    }
}

data class DetailUiState(
    val restaurant: Restaurant? = null,
    val menuItems: List<MenuWithDetails> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)