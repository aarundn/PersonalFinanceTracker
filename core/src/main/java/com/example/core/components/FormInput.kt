package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.dimensions

@Composable
fun FormInput(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    maxLine: Int = 1,
    minLine: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = MaterialTheme.dimensions.spacingSmall)
        )

        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled && onClick == null, 
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (enabled) MaterialTheme.colorScheme.onSurface 
                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(MaterialTheme.dimensions.radiusSmall)
                )
                .border(
                    width = MaterialTheme.dimensions.borderThin,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(MaterialTheme.dimensions.radiusSmall)
                )
                .then(
                    if (onClick != null) {
                       Modifier.clickable(
                           interactionSource = interactionSource,
                           indication = null, // Disable ripple for custom text field
                           onClick = onClick
                       )
                    } else Modifier
                )
                .padding(MaterialTheme.dimensions.spacingMedium),
            singleLine = keyboardType != KeyboardType.Text,
            maxLines = maxLine,
            minLines = minLine,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerTextField()
            }
        )
    }
}




@Preview(showBackground = true)
@Composable
private fun FormFormComponentsPreview() {
    PersonalFinanceTrackerTheme {
        Column(
            modifier = Modifier.padding(MaterialTheme.dimensions.spacingMedium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spacingMedium)
        ) {
            FormInput(
                label = "Title",
                value = "Sample Transaction",
                onValueChange = {},
                placeholder = "Enter transaction title"
            )

            FormInput(
                label = "Notes (Optional)",
                value = "This is a sample note",
                onValueChange = {},
                maxLine = 5,
                minLine = 3,
                placeholder = "Add any additional notes..."
            )
        }
    }
}
