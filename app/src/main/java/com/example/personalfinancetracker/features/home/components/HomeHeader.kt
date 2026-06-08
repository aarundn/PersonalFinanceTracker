package com.example.personalfinancetracker.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.AppColorTokens

@Composable
fun HomeHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyMedium,
                color = AppColorTokens.Gray400,
                textAlign = TextAlign.Start
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                color = AppColorTokens.White,
                textAlign = TextAlign.Start
            )
        }
        
        // Placeholder Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(AppColorTokens.Gray50),
            contentAlignment = Alignment.Center
        ) {
            // We can put an icon or first letter here
            Text(
                text = userName.firstOrNull()?.toString() ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = AppColorTokens.Black
            )
        }
    }
}
