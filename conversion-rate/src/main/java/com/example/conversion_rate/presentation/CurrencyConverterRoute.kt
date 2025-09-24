package com.example.conversion_rate.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CurrencyConverterRoute(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Box (Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Currency Converter Screen", modifier = modifier.clickable(onClick = onNavigateBack))
    }
}