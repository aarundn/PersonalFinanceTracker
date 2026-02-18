package com.example.personalfinancetracker.features.budget.budgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.core.components.EmptyState
import com.example.core.components.HeaderSection
import com.example.core.components.LoadingIndicator
import com.example.personalfinancetracker.features.budget.budgets.components.BudgetCard
import com.example.personalfinancetracker.features.budget.model.BudgetUi
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.ALPHA_INITIAL_VALUE
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.ALPHA_TARGET_VALUE
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.ANIMATION_DELAY
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.ANIMATION_DURATION
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.ANIMATION_SPRING_DAMPING_RATIO
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.ANIMATION_SPRING_STIFFNESS
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.X_INITIAL_VALUE
import com.example.personalfinancetracker.features.budget.utils.AnimationConstants.X_TARGET_VALUE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BudgetScreen(
    budgetsUiState: BudgetsUiState,
    onEvent: (BudgetsEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            val state = budgetsUiState as? BudgetsUiState.Success
            if (state?.budgets?.isEmpty() == false)
                HeaderSection(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = "Budget Overview",
                )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (budgetsUiState) {
                is BudgetsUiState.Loading -> LoadingIndicator()

                is BudgetsUiState.Error -> {
                    Text(
                        text = budgetsUiState.message,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                is BudgetsUiState.Success -> {
                    if (budgetsUiState.budgets.isEmpty()) {
                        EmptyState(
                            title = "No budgets yet",
                            description = "Create your first budget to start tracking your spending",
                            buttonText = "Add Budget",
                            onAddClick = { onEvent(BudgetsEvent.OnAddBudgetClick) }
                        )
                    } else {
                        Content(budgetsUiState, onEvent)

                        FloatingActionButton(
                            onClick = { onEvent(BudgetsEvent.OnAddBudgetClick) },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(24.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 8.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Budget"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    budgetsUiState: BudgetsUiState.Success,
    onEvent: (BudgetsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            itemsIndexed(
                budgetsUiState.budgets,
                key = { _, budget -> budget.id }) { index, budget ->
                AnimatedBudgetCard(
                    budget = budget,
                    index = index,
                    onClick = { onEvent(BudgetsEvent.OnBudgetClick(budget)) }
                )
            }
        }
    }
}


@Composable
private fun AnimatedBudgetCard(
    budget: BudgetUi,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasAnimated = rememberSaveable(budget.id) { mutableStateOf(false) }
    val animatedAlpha =
        remember { Animatable(if (hasAnimated.value) ALPHA_TARGET_VALUE else ALPHA_INITIAL_VALUE) }
    val animatedTranslationX =
        remember { Animatable(if (hasAnimated.value) X_TARGET_VALUE else X_INITIAL_VALUE) }

    LaunchedEffect(Unit) {
        if (!hasAnimated.value) {
            delay(index * ANIMATION_DELAY)
            launch {
                animatedAlpha.animateTo(
                    targetValue = ALPHA_TARGET_VALUE,
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                )
            }
            animatedTranslationX.animateTo(
                targetValue = X_TARGET_VALUE,
                animationSpec = spring(
                    dampingRatio = ANIMATION_SPRING_DAMPING_RATIO,
                    stiffness = ANIMATION_SPRING_STIFFNESS
                )
            )
            hasAnimated.value = true
        }
    }

    BudgetCard(
        budget = budget,
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            alpha = animatedAlpha.value
            translationX = animatedTranslationX.value
        }
    )
}
