package com.example.personalfinancetracker.features.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.conversion_rate.sync.SyncStatus
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.statusContainer
import com.example.core.utils.parseDateString

@Composable
fun SyncStatusBanner(
    syncStatus: SyncStatus,
    modifier: Modifier = Modifier
) {
    val isSyncing = syncStatus is SyncStatus.Syncing

    val baseColor = when (syncStatus) {
        is SyncStatus.Syncing -> MaterialTheme.colorScheme.onSurfaceVariant
        is SyncStatus.Success -> AppTheme.colors.income
        is SyncStatus.Failed -> MaterialTheme.colorScheme.error
        is SyncStatus.Idle -> MaterialTheme.colorScheme.error
    }

    val displayString = when (syncStatus) {
        is SyncStatus.Syncing -> "Syncing..."
        is SyncStatus.Success -> "Last sync: ${
            parseDateString(
                syncStatus.timestamp,
                isBoth = true
            )
        } \nQueued for next sync"

        is SyncStatus.Failed -> syncStatus.error
        is SyncStatus.Idle -> "Queued for sync"
    }

    SyncStatusBannerContent(
        modifier = modifier,
        baseColor = baseColor,
        text = displayString,
        isSyncing = isSyncing
    )
}

@Composable
private fun SyncStatusBannerContent(
    text: String,
    baseColor: Color,
    isSyncing: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusContainer(baseColor = baseColor, cornerRadius = AppTheme.dimensions.radiusSmall)
            .padding(horizontal = AppTheme.dimensions.spacingMediumSmall, vertical = AppTheme.dimensions.spacingSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingSmall)
        ) {
            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(AppTheme.dimensions.iconSizeSmall),
                    color = baseColor,
                    strokeWidth = AppTheme.dimensions.borderNormal
                )
            }

            Text(
                text = text,
                color = baseColor,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
