package com.example.personalfinancetracker

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.core.R
import com.example.personalfinancetracker.features.budget.navigation.BudgetRoutes
import com.example.personalfinancetracker.features.home.navigation.HomeRoutes
import com.example.personalfinancetracker.features.transaction.navigation.TransactionRoutes

data class BottomItem(
    val label: String,
    val icon: Int,
    val route: Any,
)


val mainBottomItems = listOf(
    BottomItem(
        label = "Home",
        icon = R.drawable.home,
        route = HomeRoutes.HomeRoute
    ),
    BottomItem(
        label = "Transactions",
        icon = R.drawable.dollar,
        route = TransactionRoutes.TransactionsRoute
    ),
    BottomItem(
        label = "Budgets",
        icon = R.drawable.folder,
        route = BudgetRoutes.BudgetsRoute
    )
)

@Composable
fun shouldShowBottomBar(
    navController: NavHostController,
    items: List<BottomItem>
): Boolean {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry.value?.destination ?: return true
    return items.any { item ->
        when (item.route) {
            is HomeRoutes.HomeRoute -> destination.hasRoute<HomeRoutes.HomeRoute>()
            is TransactionRoutes.TransactionsRoute -> destination.hasRoute<TransactionRoutes.TransactionsRoute>()
            is BudgetRoutes.BudgetsRoute -> destination.hasRoute<BudgetRoutes.BudgetsRoute>()
            else -> false
        }
    }
}

@Composable
fun AppBottomBar(navController: NavHostController, items: List<BottomItem>) {
    if (!shouldShowBottomBar(navController, items)) return
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry.value?.destination
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        items.forEach { item ->
            val label = item.label
            val icon = item.icon
            val selected = destination?.let { destination ->
                when (item.route) {
                    is HomeRoutes.HomeRoute -> destination.hasRoute<HomeRoutes.HomeRoute>()
                    is TransactionRoutes.TransactionsRoute -> destination.hasRoute<TransactionRoutes.TransactionsRoute>()
                    is BudgetRoutes.BudgetsRoute -> destination.hasRoute<BudgetRoutes.BudgetsRoute>()
                    else -> false
                }
            } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }

                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = icon),
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledIconColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                ),
                interactionSource = remember { MutableInteractionSource() }, // required
                modifier = Modifier.indication(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
            )
        }
    }
}
