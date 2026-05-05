package com.example.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import com.example.core.ui.theme.AppTheme
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    initialSelectedDateMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimensions.spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimensions.spacingLarge,
                        vertical = AppTheme.dimensions.spacingMedium
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "اختر التاريخ",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                IconButton(
                    modifier = Modifier
                        .size(AppTheme.dimensions.iconSizeSmall)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        ),
                    onClick = onDismiss
                ) {
                    Icon(
                        modifier = Modifier.size(AppTheme.dimensions.iconSizeSmall),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimensions.spacingLarge),
                text = "١٥ أكتوبر ٢٠٢٣",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    modifier = modifier
                        .zIndex(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomDatePicker(
                        modifier = modifier
                            .zIndex(1f)
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        list = yearsList.map { it.toString() }
                    )
                    CustomDatePicker(
                        modifier = modifier
                            .zIndex(1f)
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        list = monthsListInArabic
                    )
                    CustomDatePicker(
                        modifier = modifier
                            .zIndex(1f)
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        list = daysList.map { it.toString() }
                    )

                }
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusLarge)
                        )
                        .align(Alignment.Center)
                        .border(
                            width = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(AppTheme.dimensions.radiusLarge)
                        )
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = AppTheme.dimensions.spacingExtraLarge
                    ),
                onClick = {}
            )
        }
    }

}

@Composable
fun CustomDatePicker(
    modifier: Modifier = Modifier,
    list: List<String> = daysList.map { it.toString() }
) {

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { list.size }
    )
    val itemHeight = 48.dp
    val visibleItemsCount = 5
    val containerHeight = itemHeight * visibleItemsCount
    val verticalPadding = itemHeight * 2
    VerticalPager(
        state = pagerState,
        modifier = modifier.height(containerHeight),
        contentPadding = PaddingValues(vertical = verticalPadding),
        pageSize = PageSize.Fixed(itemHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) { page ->

        val pageOffset = pagerState.getOffsetDistanceInPages(page)

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(itemHeight)
                .graphicsLayer {
                    val absoluteOffset = abs(pageOffset)
                    val clampedOffset = absoluteOffset.coerceAtMost(2f)
                    alpha = 1f - (clampedOffset * 0.4f)
                    scaleX = 1f - (clampedOffset * 0.15f)
                    scaleY = scaleX

                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = list[page],
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = if (pageOffset == 0f) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}


val daysList = (1..31).toList()

// Months: Standard Gregorian months in Arabic
val monthsListInArabic = listOf(
    "يناير",     // January
    "فبراير",    // February
    "مارس",      // March
    "أبريل",     // April
    "مايو",      // May
    "يونيو",     // June
    "يوليو",     // July
    "أغسطس",     // August
    "سبتمبر",    // September
    "أكتوبر",    // October
    "نوفمبر",    // November
    "ديسمبر"     // December
)

// Years: A reasonable range for a picker (e.g., 2020 to 2030)
val yearsList = (2020..2030).toList()


// 2. Data Class for the Selected Result

/**
 * Use this data class to hold the final selection or to pass pre-selected
 * test dates to your UI.
 */
data class SelectedArabicDate(
    val day: Int,
    val month: String,
    val year: Int
)

// 3. A list of specific test cases to check your UI behavior
val testDatesList = listOf(
    SelectedArabicDate(day = 15, month = "أكتوبر", year = 2023), // From your image
    SelectedArabicDate(day = 1, month = "يناير", year = 2024),   // Start of the year test
    SelectedArabicDate(day = 29, month = "فبراير", year = 2024), // Leap year test
    SelectedArabicDate(day = 31, month = "ديسمبر", year = 2025), // End of year test
    SelectedArabicDate(day = 15, month = "مايو", year = 2026)    // Future date test
)

@Preview
@Composable
private fun DatePickerDialogPreview() {
    PersonalFinanceTrackerTheme {
        CustomDatePickerDialog(
            initialSelectedDateMillis = null,
            onDateSelected = {},
            onDismiss = {}
        )
    }
}

@Composable
fun exmapel(modifier: Modifier = Modifier) {
    Card() { }
}