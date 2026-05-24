package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun IconWrapper(modifier: Modifier = Modifier, icon: ImageVector, iconColor: Color) {
    Box(
        modifier = modifier.background(
            color = Color.Transparent,
            shape = RoundedCornerShape(AppTheme.dimensions.radiusLarge)
        ).border(
            width = AppTheme.dimensions.borderThin,
            color = iconColor.copy(alpha = 0.2f),
            shape = RoundedCornerShape(AppTheme.dimensions.radiusLarge)
        ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .matchParentSize()
                .background(
                    brush = Brush.linearGradient(
                        start = Offset.Infinite,
                        end = Offset.Zero,
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.1f),
                            iconColor.copy(alpha = 0.2f),
                        )
                    ),
                    shape = RoundedCornerShape(AppTheme.dimensions.radiusLarge)
                )
                .blur(
                    radius = 10.dp,
                    edgeTreatment = BlurredEdgeTreatment.Rectangle
                )
            )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}


@Preview
@Composable
private fun IconWrapperPreview() {
    PersonalFinanceTrackerTheme {
        IconWrapper(
            icon = ImageVector.vectorResource(R.drawable.grocories),
            iconColor = AppTheme.colors.categoryGroceries,
            modifier = Modifier.size(AppTheme.dimensions.iconSizeMediumLarge)
        )
    }
}