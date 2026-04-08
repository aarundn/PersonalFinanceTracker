package com.example.personalfinancetracker.features.home.components


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.ProgressBackground

@Composable
fun CustomCircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    animDuration: Int = 1000,
    progressColor: Color = Color.Red,
    trackColor: Color = ProgressBackground,
    strokeWidth: Dp = 8.dp

) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = FastOutSlowInEasing
        ),
        label = "progress_animation"
    )

    Canvas(modifier = modifier.size(80.dp).padding(10.dp)) {
        
        val strokeWidthPx = strokeWidth.toPx()
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidthPx)
        )
        drawArc(
            color = progressColor,
            startAngle = 90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = Stroke(
                width = strokeWidthPx,
                cap = StrokeCap.Round
            )
        )
    }
}


@Preview
@Composable
private fun IndicatorPreview() {
    PersonalFinanceTrackerTheme {
        CustomCircularProgressBar(progress = 0.8f)
    }
}