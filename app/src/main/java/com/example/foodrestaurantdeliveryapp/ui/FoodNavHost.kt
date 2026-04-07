package com.example.foodrestaurantdeliveryapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.foodrestaurantdeliveryapp.ui.screen.AuthScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.DetailScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.FoodProductSearchScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.HomeScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.MenuEntryEditScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.SettingsScreen

@Composable
fun FoodNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                navigateToDetail = { restaurantId -> navController.navigate("detail/$restaurantId") },
                navigateToSettings = { navController.navigate("settings") },
                navigateToProductSearch = { navController.navigate("food_product_search") },
                navigateToAuth = {
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "detail/{restaurantId}",
            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
            DetailScreen(
                navigateBack = { navController.popBackStack() },
                onAddMenuItem = { restId -> navController.navigate("add_menu_item/$restId") },
                onEditMenuItem = { menuId -> navController.navigate("edit_menu_item/$menuId") }
            )
        }

        composable("food_product_search") {
            FoodProductSearchScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "add_menu_item/{restaurantId}",
            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
        ) { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")
            if (restaurantId != null) {
                MenuEntryEditScreen(navigateBack = { navController.popBackStack() })
            } else {
                navController.popBackStack()
            }
        }

        composable(
            route = "edit_menu_item/{menuId}",
            arguments = listOf(navArgument("menuId") { type = NavType.StringType })
        ) { backStackEntry ->
            val menuId = backStackEntry.arguments?.getString("menuId")
            if (menuId != null) {
                MenuEntryEditScreen(navigateBack = { navController.popBackStack() })
            } else {
                navController.popBackStack()
            }
        }

        composable("settings") {
            SettingsScreen(navigateBack = { navController.popBackStack() })
        }
    }
}