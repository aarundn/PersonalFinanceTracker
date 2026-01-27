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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
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
import com.example.personalfinancetracker.features.budget.budgets.components.BudgetCard
import com.example.personalfinancetracker.features.budget.budgets.components.EmptyState
import com.example.personalfinancetracker.features.budget.budgets.components.HeaderSection
import com.example.personalfinancetracker.features.budget.budgets.components.OverallSummaryCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BudgetScreen(
    state: BudgetContract.State,
    onEvent: (BudgetContract.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            state.budgets.isEmpty() -> {
                EmptyState(
                    onAddClick = { onEvent(BudgetContract.Event.OnAddBudgetClick) }
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    HeaderSection(
                        title = "Budget Overview",
                        onAddClick = { onEvent(BudgetContract.Event.OnAddBudgetClick) }
                    )

                    // Overall Summary Card
                    OverallSummaryCard(
                        totalBudgeted = state.totalBudgeted,
                        totalSpent = state.totalSpent,
                        overallProgress = state.overallProgress
                    )

                    // Budget Categories List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 88.dp) // Space for FAB
                    ) {
                        itemsIndexed(
                            state.budgets,
                            key = { _, budget -> budget.id }) { index, budget ->
                            AnimatedBudgetCard(
                                budget = budget,
                                index = index,
                                onClick = { onEvent(BudgetContract.Event.OnBudgetClick(budget)) }
                            )
                        }
                    }
                }

                // Floating Action Button
                FloatingActionButton(
                    onClick = { onEvent(BudgetContract.Event.OnAddBudgetClick) },
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

        // Error Handling
        state.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

const val ALPHA_INITIAL_VALUE = 0f
const val ALPHA_TARGET_VALUE = 1f

const val X_INITIAL_VALUE = 200f
const val X_TARGET_VALUE = 0f


const val ANIMATION_DURATION = 400
const val ANIMATION_DELAY = 100L

const val ANIMATION_SPRING_DAMPING_RATIO = 0.7f
const val ANIMATION_SPRING_STIFFNESS = 60f


@Composable
private fun AnimatedBudgetCard(
    budget: BudgetContract.Budget,
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
