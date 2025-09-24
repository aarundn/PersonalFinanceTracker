package com.example.personalfinancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.conversion_rate.navigation.currencyConverterScreen
import com.example.core.navigation.features.BudgetFeature
import com.example.core.navigation.features.HomeFeature
import com.example.core.navigation.features.TransactionFeature
import com.example.core.navigation.register
import com.example.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalFinanceTrackerTheme {
                val navController = rememberNavController()

                Scaffold { paddingValues ->
                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {


    val homeFeature: HomeFeature = koinInject()
    val budgetFeature: BudgetFeature = koinInject()
    val transactionFeature: TransactionFeature = koinInject()


    NavHost(
        navController = navController,
        startDestination = homeFeature.homeRoute(),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            scaleOut(
                targetScale = 0.98f,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            scaleIn(
                initialScale = 0.98f,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            ) + fadeOut(tween(500))
        },
    ) {
        register(homeFeature, navController, modifier)
        register(transactionFeature, navController, modifier)
        register(budgetFeature, navController, modifier)
        currencyConverterScreen(onNavigateBack = { navController.popBackStack() })
    }
}
