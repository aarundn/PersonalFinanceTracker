package com.example.personalfinancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.conversion_rate.navigation.currencyConverterScreen
import com.example.core.navigation.Feature
import com.example.core.navigation.register
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.personalfinancetracker.features.home.navigation.HomeRoutes
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalFinanceTrackerTheme {

                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val destination = navBackStackEntry.value?.destination

                val allFeatures: List<Feature> = getKoin().getAll<Feature>()

                Scaffold(bottomBar = {
                    AnimatedVisibility(
                        visible = shouldShowBottomBar(destination),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        AppBottomBar(navController = navController, destination)
                    }
                }) { paddingValues ->

                    AppNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues),
                        features = allFeatures
                    )
                }
            }
        }
    }
}


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    features: List<Feature> = emptyList()
) {

    NavHost(
        navController = navController,
        startDestination = HomeRoutes.HomeRoute,
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
        features.forEach {
            register(it, navController, modifier)
        }
        currencyConverterScreen(onNavigateBack = { navController.popBackStack() })
    }
}
