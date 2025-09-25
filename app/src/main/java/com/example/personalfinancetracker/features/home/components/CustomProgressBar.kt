package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.ui.theme.ProgressBackground
import com.example.personalfinancetracker.ui.theme.ProgressPrimary

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ProgressBackground,
    progressColor: Color = ProgressPrimary,
    height: androidx.compose.ui.unit.Dp = 8.dp
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .height(height)
                .clip(RoundedCornerShape(4.dp))
                .background(progressColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomProgressBarPreview() {
    CustomProgressBar(
        progress = 0.6f,
        modifier = Modifier.fillMaxWidth()
    )
}
