package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.repository.auth.AuthRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.category.CategoryRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.food.FoodRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.MenuEntryRepository
import com.example.foodrestaurantdeliveryapp.utils.SearchTokenGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuEntryEditViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val menuEntryRepository: MenuEntryRepository,
    private val categoryRepository: CategoryRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val restaurantId: String? = savedStateHandle["restaurantId"]
    private val menuId: String? = savedStateHandle["menuId"]

    private val _uiState = MutableStateFlow(MenuEntryEditUiState())
    val uiState: StateFlow<MenuEntryEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (!authRepository.isSignedIn()) {
                _uiState.update { it.copy(errorMessage = "Authentication required", isLoading = false) }
                return@launch
            }
            loadCategories()
            when {
                menuId != null -> loadForEdit(menuId)
                restaurantId != null -> setupAddMode(restaurantId)
                else -> setInvalidArgumentsError()
            }
        }
    }

    private suspend fun loadCategories() {
        val categories: List<Category> = categoryRepository.getAllCategories().first()
        _uiState.update { it.copy(categories = categories) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateImageUrl(url: String) {
        _uiState.update { it.copy(imageUrl = url) }
    }

    fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun toggleAvailable() {
        _uiState.update { it.copy(isAvailable = !it.isAvailable) }
    }

    fun selectCategory(categoryId: String) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun save(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!authRepository.isSignedIn()) {
            onError("Please sign in to save changes")
            return
        }
        if (!validate()) return onError("Please fill all required fields")

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                when (_uiState.value.mode) {
                    MenuEntryEditUiState.Mode.ADD -> performAdd()
                    MenuEntryEditUiState.Mode.EDIT -> performEdit()
                }
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false) }
                onError("Save failed: ${e.message}")
            }
        }
    }

    private suspend fun loadForEdit(menuId: String) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val entry = menuEntryRepository.getMenuEntryWithFood(menuId)
            if (entry != null) {
                _uiState.update {
                    it.copy(
                        mode = MenuEntryEditUiState.Mode.EDIT,
                        menuId = menuId,
                        name = entry.foodItem.name,
                        description = entry.foodItem.description,
                        imageUrl = entry.foodItem.imageUrl,
                        price = entry.menuEntry.price.toString(),
                        isAvailable = entry.menuEntry.isAvailable,
                        selectedCategoryId = entry.foodItem.categoryId,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(errorMessage = "Entry not found", isLoading = false) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to load data", isLoading = false) }
        }
    }

    private fun setupAddMode(restaurantId: String) {
        _uiState.update {
            it.copy(
                mode = MenuEntryEditUiState.Mode.ADD,
                restaurantId = restaurantId,
                isLoading = false
            )
        }
    }

    private fun setInvalidArgumentsError() {
        _uiState.update { it.copy(errorMessage = "Invalid arguments", isLoading = false) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        val errors = mutableMapOf<String, String>()
        if (state.name.isBlank()) errors["name"] = "Name is required"
        if (state.price.isBlank()) errors["price"] = "Price is required"
        if (state.selectedCategoryId == null) errors["category"] = "Category is required"
        if (state.price.isNotBlank() && state.price.toDoubleOrNull() == null) {
            errors["price"] = "Price must be a valid number"
        }
        _uiState.update { it.copy(validationErrors = errors) }
        return errors.isEmpty()
    }

    private suspend fun performAdd() {
        val state = _uiState.value
        val restaurantId = requireNotNull(state.restaurantId) { "Restaurant ID missing" }
        val categoryId = requireNotNull(state.selectedCategoryId) { "Category not selected" }
        val priceValue = state.price.toDoubleOrNull()
            ?: throw IllegalArgumentException("Invalid price format")

        val categoryName = state.categories.find { it.categoryId == categoryId }?.name
            ?: throw IllegalStateException("Category not found")

        val foodItem = FoodItem(
            categoryId = categoryId,
            categoryName = categoryName,
            name = state.name,
            description = state.description,
            imageUrl = state.imageUrl,
            searchTokens = SearchTokenGenerator.generateTokens(state.name) +
                    SearchTokenGenerator.generateTokens(state.description)
        )
        val foodId = foodRepository.insertFoodItem(foodItem)

        val menuEntry = MenuEntry(
            restaurantId = restaurantId,
            restaurantName = "",
            foodId = foodId,
            foodName = state.name,
            foodDescription = state.description,
            foodImageUrl = state.imageUrl,
            price = priceValue,
            isAvailable = state.isAvailable,
            categoryId = categoryId,
            category = categoryName,
            searchTokens = SearchTokenGenerator.generateTokens(state.name)
        )
        menuEntryRepository.insertMenuEntry(menuEntry)
    }

    private suspend fun performEdit() {
        val state = _uiState.value
        val menuId = requireNotNull(state.menuId) { "Menu ID missing" }
        val categoryId = requireNotNull(state.selectedCategoryId) { "Category not selected" }
        val priceValue = state.price.toDoubleOrNull()
            ?: throw IllegalArgumentException("Invalid price format")

        val existing = requireNotNull(menuEntryRepository.getMenuEntryWithFood(menuId)) {
            "Menu entry not found"
        }

        val categoryName = state.categories.find { it.categoryId == categoryId }?.name
            ?: existing.foodItem.categoryName

        val updatedFood = existing.foodItem.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            name = state.name,
            description = state.description,
            imageUrl = state.imageUrl,
            searchTokens = SearchTokenGenerator.generateTokens(state.name) +
                    SearchTokenGenerator.generateTokens(state.description)
        )
        foodRepository.updateFoodItem(updatedFood)

        val updatedMenu = existing.menuEntry.copy(
            price = priceValue,
            isAvailable = state.isAvailable,
            foodName = state.name,
            foodDescription = state.description,
            foodImageUrl = state.imageUrl,
            categoryId = categoryId,
            category = categoryName,
            searchTokens = SearchTokenGenerator.generateTokens(state.name)
        )
        menuEntryRepository.updateMenuEntry(updatedMenu)
    }
}

data class MenuEntryEditUiState(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val isAvailable: Boolean = true,
    val mode: Mode = Mode.ADD,
    val restaurantId: String? = null,
    val menuId: String? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap()
) {
    enum class Mode { ADD, EDIT }
    val isAddMode: Boolean get() = mode == Mode.ADD
    val isEditMode: Boolean get() = mode == Mode.EDIT
}