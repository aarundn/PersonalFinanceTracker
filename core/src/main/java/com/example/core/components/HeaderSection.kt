package com.example.core.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.R
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    showBackIcon: Boolean = true,
    title: String = "",
    actions: @Composable (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (showBackIcon)
                Row(
                    modifier = Modifier
                        .padding(start = AppTheme.dimensions.spacingExtraSmall)
                        .clickable(onClick = onBackClick),
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingExtraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(AppTheme.dimensions.iconSizeNormal)
                    )
                    Text(
                        text = stringResource(R.string.action_back),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
        },
        actions = {
            actions?.invoke()
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    onAddClick: () -> Unit = {}
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
            onClick = onAddClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HeaderSectionPreview() {
    PersonalFinanceTrackerTheme {
        Header(
            modifier = Modifier.padding(16.dp),
            title = "المعاملات",
            description = "تتبع خططك المالية بذكاء"
        )
    }
}


