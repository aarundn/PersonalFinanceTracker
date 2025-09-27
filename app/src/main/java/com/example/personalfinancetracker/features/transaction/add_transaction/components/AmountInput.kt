package com.example.personalfinancetracker.features.transaction.add_transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalfinancetracker.features.transaction.add_transaction.AddTransactionContract
import com.example.personalfinancetracker.features.transaction.add_transaction.utils.TextFormattingUtils
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@Composable
fun AmountInput(
    amount: String,
    currency: String,
    convertedAmount: Double?,
    isConverting: Boolean,
    onAmountChanged: (String) -> Unit,
    onConvertCurrency: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Amount (${AddTransactionContract.PopularCurrencies.currencies.find { it.code == currency }?.symbol ?: "$"})",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        BasicTextField(
            value = amount,
            onValueChange = onAmountChanged,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer, // Light gray background
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (amount.isEmpty()) {
                    Text(
                        text = "0.00",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerTextField()
            }
        )
        
        // Currency Conversion Display
        if (currency != "USD" && amount.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFEFF6FF), // Blue-50 equivalent
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFBFDBFE), // Blue-200 equivalent
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
                    .padding(top = 8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Convert",
                                tint = Color(0xFF2563EB), // Blue-600
                                modifier = Modifier.padding(2.dp)
                            )
                            Text(
                                text = "Converted to USD:",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF2563EB) // Blue-600
                            )
                        }
                        
                        Button(
                            onClick = onConvertCurrency,
                            enabled = !isConverting,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color(0xFF2563EB)
                            ),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            if (isConverting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(2.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFF2563EB)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    }
                    
                    Text(
                        text = if (isConverting) {
                            "Converting..."
                        } else if (convertedAmount != null) {
                            TextFormattingUtils.formatCurrency(convertedAmount)
                        } else {
                            "Error converting"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF1D4ED8), // Blue-700
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Text(
                        text = "Live exchange rate applied",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF2563EB), // Blue-600
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AmountInputPreview() {
    PersonalFinanceTrackerTheme {
        AmountInput(
            amount = "100.00",
            currency = "EUR",
            convertedAmount = 85.0,
            isConverting = false,
            onAmountChanged = {},
            onConvertCurrency = {}
        )
    }
}
