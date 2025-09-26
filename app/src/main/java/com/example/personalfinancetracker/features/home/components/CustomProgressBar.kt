package com.example.personalfinancetracker.features.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.ui.components.CustomProgressBar as SharedCustomProgressBar

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = com.example.personalfinancetracker.ui.theme.ProgressBackground,
    progressColor: Color = com.example.personalfinancetracker.ui.theme.ProgressPrimary,
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
