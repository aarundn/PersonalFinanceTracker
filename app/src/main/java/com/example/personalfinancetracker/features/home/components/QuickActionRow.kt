package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.core.ui.theme.AppColorTokens
import com.example.core.ui.theme.AppTheme

@Composable
fun QuickActionRow(
    onAddClick: () -> Unit,
    onTransferClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMedium)
    ) {
        QuickActionButton(
            text = stringResource(com.example.personalfinancetracker.R.string.home_action_transfer),
            icon = Icons.Default.Send,
            onClick = onTransferClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            text = stringResource(com.example.personalfinancetracker.R.string.home_action_add),
            icon = Icons.Default.Add,
            onClick = onAddClick,
            modifier = Modifier.weight(1f),
            isPrimary = true
        )
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    val backgroundColor = if (isPrimary) AppColorTokens.Blue500 else AppColorTokens.DarkSurface
    val contentColor = if (isPrimary) AppColorTokens.White else AppColorTokens.Gray50

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusMedium))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.dimensions.spacingMediumSmall),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(AppTheme.dimensions.iconSizeSmall)
        )
        Spacer(modifier = Modifier.width(AppTheme.dimensions.spacingSmall))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}
