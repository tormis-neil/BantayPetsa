package com.bantaypetsa.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null
) {
    object Splash : Screen(
        route = "splash",
        title = "Splash"
    )

    object SignIn : Screen(
        route = "sign_in",
        title = "Sign In"
    )

    object Home : Screen(
        route = "home",
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Products : Screen(
        route = "products",
        title = "Products",
        selectedIcon = Icons.Filled.Inventory2,
        unselectedIcon = Icons.Outlined.Inventory2
    )

    object Alerts : Screen(
        route = "alerts?filter={filter}",
        title = "Alerts",
        selectedIcon = Icons.Filled.NotificationsActive,
        unselectedIcon = Icons.Outlined.NotificationsActive
    ) {
        fun createRoute(filter: String = "All") = "alerts?filter=$filter"
    }

    object History : Screen(
        route = "history",
        title = "History",
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    )

    object Profile : Screen(
        route = "profile",
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    object Settings : Screen(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    // Supporting screens
    object ProductDetail : Screen(
        route = "product_detail/{productId}",
        title = "Product Details"
    ) {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object AddEditProduct : Screen(
        route = "add_edit_product?productId={productId}",
        title = "Product"
    ) {
        fun createRoute(productId: String? = null) =
            if (productId != null) "add_edit_product?productId=$productId" else "add_edit_product"
    }

    object AddEditBatch : Screen(
        route = "add_edit_batch/{productId}?batchId={batchId}",
        title = "Batch"
    ) {
        fun createRoute(productId: String, batchId: String? = null) =
            if (batchId != null) "add_edit_batch/$productId?batchId=$batchId" else "add_edit_batch/$productId"
    }

    object RecordStockAction : Screen(
        route = "stock_action/{batchId}",
        title = "Stock Action"
    ) {
        fun createRoute(batchId: String) = "stock_action/$batchId"
    }

    companion object {
        val bottomNavItems = listOf(Home, Products, Alerts, History, Profile)
    }
}
