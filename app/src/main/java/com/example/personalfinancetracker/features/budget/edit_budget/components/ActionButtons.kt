package com.example.personalfinancetracker.features.budget.edit_budget.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun ActionButtons(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean,
    isSaveEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Cancel")
        }

        Button(
            onClick = onSave,
            enabled = isSaveEnabled && !isLoading,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Save",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(text = "Save Changes")
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
            isLoading = false,
            isSaveEnabled = true
        )
    }
}

