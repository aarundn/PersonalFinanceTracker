package com.example.personalfinancetracker.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.personalfinancetracker.features.budget.budgets.navigation.navigateToBudgetScreen

@Composable
fun HomeRoute(navController: NavController, onNavigateToCurrency: () -> Unit ) {
    Column (Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "HomeScreen",
            Modifier.clickable(onClick = { navController.navigateToBudgetScreen() })
        )
        Text(
            text = "HomeScreen2",
            Modifier.clickable(onClick = { onNavigateToCurrency() })
        )
    }
}