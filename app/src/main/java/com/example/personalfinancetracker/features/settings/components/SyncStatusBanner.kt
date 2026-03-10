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
import androidx.compose.ui.unit.dp
import com.example.conversion_rate.sync.SyncStatus
import com.example.core.ui.theme.Expense
import com.example.core.ui.theme.Income
import com.example.core.ui.theme.MutedForeground
import com.example.core.ui.theme.statusContainer
import com.example.core.utils.parseDateString

@Composable
fun SyncStatusBanner(
    syncStatus: SyncStatus,
    modifier: Modifier = Modifier
) {
    val isSyncing = syncStatus is SyncStatus.Syncing

    val baseColor = when (syncStatus) {
        is SyncStatus.Syncing -> MutedForeground
        is SyncStatus.Success -> Income
        is SyncStatus.Failed -> Expense
        is SyncStatus.Idle -> Expense
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
            .statusContainer(baseColor = baseColor, cornerRadius = 8.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isSyncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = baseColor,
                    strokeWidth = 2.dp
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
