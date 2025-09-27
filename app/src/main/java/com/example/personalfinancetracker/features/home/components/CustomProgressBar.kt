package com.example.personalfinancetracker.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.ProgressBackground
import com.example.core.ui.theme.ProgressPrimary
import com.example.core.components.CustomProgressBar as SharedCustomProgressBar

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ProgressBackground,
    progressColor: Color = ProgressPrimary,
    height: Dp = 8.dp
) {
    SharedCustomProgressBar(
        progress = progress,
        modifier = modifier,
        backgroundColor = backgroundColor,
        progressColor = progressColor,
        height = height
    )
}
