package com.example.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.R
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.dimensions

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimensions.spacingMedium),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.action_cancel),
                modifier = Modifier.padding(end = MaterialTheme.dimensions.spacingSmall)
            )
            Text(stringResource(R.string.action_cancel))
        }
        
        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2563EB), // Blue-600
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = MaterialTheme.dimensions.spacingSmall),
                    strokeWidth = MaterialTheme.dimensions.borderNormal,
                    color = Color.White
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.action_save),
                    modifier = Modifier.padding(end = MaterialTheme.dimensions.spacingSmall)
                )
            }
            Text(stringResource(R.string.action_save))
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
        )
    }
}


