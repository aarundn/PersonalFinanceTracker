package com.example.personalfinancetracker.features.budget.edit_budget.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.core.ui.theme.dimensions
import com.example.core.R
import com.example.core.ui.theme.ProgressError

@Composable
fun DangerZoneCard(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(MaterialTheme.dimensions.borderThin, ProgressError.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimensions.spacingLarge),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
        ) {
            Text(
                text = stringResource(R.string.budget_danger_zone),
                style = MaterialTheme.typography.titleMedium,
                color = ProgressError,
                fontWeight = FontWeight.SemiBold
            )
            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProgressError,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(end = MaterialTheme.dimensions.spacingSmall)
                )
                Text(stringResource(R.string.budget_delete_button))
            }
            Text(
                text = "This action cannot be undone. This will permanently delete your budget.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}