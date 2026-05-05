package com.example.personalfinancetracker.features.budget.add_budget.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun ActionButtons(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean,
    isSaveEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = AppTheme.dimensions.spacingMedium),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingMediumSmall)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Cancel",
                modifier = Modifier.padding(end = AppTheme.dimensions.spacingSmall)
            )
            Text(text = "Cancel")
        }

        Button(
            onClick = onSave,
            enabled = isSaveEnabled && !isSaving,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = AppTheme.dimensions.spacingSmall),
                    strokeWidth = AppTheme.dimensions.borderNormal,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Save",
                    modifier = Modifier.padding(end = AppTheme.dimensions.spacingSmall)
                )
            }
            Text(text = "Save Budget")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionButtonsPreview() {
    PersonalFinanceTrackerTheme {
        ActionButtons(
            onCancel = {},
            onSave = {},
            isSaving = false,
            isSaveEnabled = true
        )
    }
}


