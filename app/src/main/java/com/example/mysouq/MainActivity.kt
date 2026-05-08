package com.example.mysouq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mysouq.ui.components.AppLogo
import com.example.mysouq.ui.navigation.Screen
import com.example.mysouq.ui.screens.*
import com.example.mysouq.ui.theme.MySouqTheme
import com.example.mysouq.ui.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()

            MySouqTheme(darkTheme = isDarkMode) {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val cartViewModel: CartViewModel = hiltViewModel()
    val cartItemCount by cartViewModel.itemCount.collectAsState()

    val items = remember {
        listOf(
            NavigationItem("Accueil", Screen.Home.route, Icons.Default.Home),
            NavigationItem("Favoris", Screen.Favorites.route, Icons.Default.Favorite),
            NavigationItem("Panier", Screen.Cart.route, Icons.Default.ShoppingCart),
            NavigationItem("Profil", Screen.Profile.route, Icons.Default.Person)
        )
    }

    var showMenu by remember { mutableStateOf(false) }

    // On ne montre pas la TopBar et la BottomBar sur l'écran Splash
    val isSplashScreen = currentDestination?.route == Screen.Splash.route

    Scaffold(
        topBar = {
            if (!isSplashScreen) {
                TopAppBar(
                    title = { AppLogo(modifier = Modifier.size(32.dp), showText = false) },
                    actions = {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Paramètres") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Screen.Settings.route)
                                },
                                leadingIcon = { Icon(Icons.Default.Settings, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("À propos") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Screen.About.route)
                                },
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            val showBottomBar = remember(currentDestination) {
                !isSplashScreen && items.any { item -> currentDestination?.hierarchy?.any { it.route == item.route } == true }
            }
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        val isCart = item.route == Screen.Cart.route
                        NavigationBarItem(
                            icon = {
                                if (isCart && cartItemCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge {
                                                Text(cartItemCount.toString())
                                            }
                                        }
                                    ) {
                                        Icon(item.icon, contentDescription = item.label)
                                    }
                                } else {
                                    Icon(item.icon, contentDescription = item.label)
                                }
                            },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Home.route) {
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = viewModel,
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
                )
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
                val viewModel: ProductDetailViewModel = hiltViewModel()
                ProductDetailScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Favorites.route) {
                val viewModel: FavoritesViewModel = hiltViewModel()
                FavoritesScreen(
                    viewModel = viewModel,
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(viewModel = cartViewModel)
            }
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    viewModel = viewModel,
                    onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                    onNavigateToAddresses = { navController.navigate(Screen.Addresses.route) },
                    onNavigateToPayments = { navController.navigate(Screen.Payments.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onLoginClick = { navController.navigate(Screen.Home.route) } // Placeholder
                )
            }
            composable(Screen.Orders.route) {
                val viewModel: OrdersViewModel = hiltViewModel()
                OrdersScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Addresses.route) {
                val viewModel: AddressesViewModel = hiltViewModel()
                AddressesScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Payments.route) {
                val viewModel: PaymentsViewModel = hiltViewModel()
                PaymentsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Settings.route) {
                val viewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(viewModel = viewModel)
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
        }
    }
}

data class NavigationItem(val label: String, val route: String, val icon: ImageVector)
