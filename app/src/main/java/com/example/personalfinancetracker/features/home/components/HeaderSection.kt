package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.personalfinancetracker.features.home.HomeContract
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun HeaderSection(
    state: HomeContract.State,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = state.greeting,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = state.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderSectionPreview() {
    PersonalFinanceTrackerTheme {
        HeaderSection(
            state = HomeContract.State()
        )
    }
}
