package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun CircleOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(320.dp)
            .blur(
                radius = 64.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
            .background(
                color = AppTheme.colors.surfaceOverlay,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun CircleOverlayPrev() {
    PersonalFinanceTrackerTheme {
        CircleOverlay(
            modifier = Modifier.padding(16.dp)
        )
    }
}