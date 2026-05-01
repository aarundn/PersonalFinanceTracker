package com.example.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.dimensions

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier.height(MaterialTheme.dimensions.buttonHeightLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(MaterialTheme.dimensions.radiusLarge),
        onClick = onClick,

        ) {
        Text(
            text = "تأكيد",
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 18.sp,
            )
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
)
@Composable
private fun PrimaryButtonPreview() {
    PersonalFinanceTrackerTheme {
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}