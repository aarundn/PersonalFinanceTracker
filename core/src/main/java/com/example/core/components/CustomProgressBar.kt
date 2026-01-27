package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressBackground
import com.example.core.ui.theme.ProgressPrimary

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ProgressBackground,
    progressColor: Color = ProgressPrimary,
    height: Dp = 8.dp
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = tween(durationMillis = 800, easing = FastOutLinearInEasing))

    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress.value)
                .height(height)
                .clip(RoundedCornerShape(4.dp))
                .background(progressColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomProgressBarPreview() {
    PersonalFinanceTrackerTheme {
        CustomProgressBar(
            progress = 0.6f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

