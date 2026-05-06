package com.example.mysouq.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object About : Screen("about")
    object Orders : Screen("orders")
    object Addresses : Screen("addresses")
    object Payments : Screen("payments")
}
