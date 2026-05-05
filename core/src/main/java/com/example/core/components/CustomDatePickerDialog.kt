package com.example.core.components

import android.util.Log
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.core.ui.theme.AppTheme
import com.example.core.ui.theme.PersonalFinanceTrackerTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.text.DateFormatSymbols
import kotlin.math.abs

private val localizedMonths: List<String> = DateFormatSymbols.getInstance().months.take(12)

private val years: List<Int> = (1950..2100).toList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    initialSelectedDateMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {

    val initial: LocalDate =
        initialSelectedDateMillis
            ?.let { Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()).date }
            ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val yearPagerState = rememberPagerState(
        initialPage = years.indexOf(initial.year).coerceAtLeast(0),
        pageCount = { years.size }
    )

    val monthPagerState = rememberPagerState(
        initialPage = initial.monthNumber - 1,
        pageCount = { localizedMonths.size }
    )

    val daysInMonth by remember(yearPagerState.currentPage, monthPagerState.currentPage) {
        derivedStateOf {
            val year  = years[yearPagerState.currentPage]
            val month = monthPagerState.currentPage + 1
            LocalDate(year, month, 1)
                .plus(1, DateTimeUnit.MONTH)
                .minus(1, DateTimeUnit.DAY)
                .dayOfMonth
        }
    }

    val dayPagerState = rememberPagerState(
        initialPage = (initial.dayOfMonth - 1).coerceAtMost(daysInMonth - 1),
        pageCount = { daysInMonth }
    )


    LaunchedEffect(daysInMonth) {
        if (dayPagerState.currentPage >= daysInMonth) {
            dayPagerState.animateScrollToPage(daysInMonth - 1)
        }
    }

    val formattedDate by remember(
        yearPagerState.currentPage,
        monthPagerState.currentPage,
        dayPagerState.currentPage
    ) {
        derivedStateOf {
            val day   = (dayPagerState.currentPage + 1).coerceAtMost(daysInMonth)
            val month = localizedMonths[monthPagerState.currentPage]
            val year  = years[yearPagerState.currentPage]
            "$day $month $year"
        }
    }

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
                    text = "Choose date",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                IconButton(
                    modifier = Modifier
                        .size(AppTheme.dimensions.iconSizeSmall)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
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
                text = formattedDate,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimensions.spacingMedium),
            ) {
                Row(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimensions.spacingMedium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WheelColumn(pagerState = dayPagerState,   items = (1..daysInMonth).map { it.toString() }, modifier = Modifier.weight(1f))
                    WheelColumn(pagerState = monthPagerState, items = localizedMonths,                         modifier = Modifier.weight(2f))
                    WheelColumn(pagerState = yearPagerState,  items = years.map { it.toString() },             modifier = Modifier.weight(1f))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 56.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(AppTheme.dimensions.radiusLarge))
                        .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(AppTheme.dimensions.radiusLarge))
                        .align(Alignment.Center)
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimensions.spacingExtraLarge),
                onClick = {
                    val day   = (dayPagerState.currentPage + 1).coerceAtMost(daysInMonth)
                    val month = monthPagerState.currentPage + 1 // 1-based for LocalDate
                    val year  = years[yearPagerState.currentPage]

                    val selectedDate = LocalDate(year, month, day)
                    onDateSelected(selectedDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds())
                    onDismiss()
                }
            )
        }
    }
}

/**
 * WHY a separate, stateless composable?
 * WheelColumn has zero knowledge of what "Year", "Month", or "Day" means.
 * It just renders a scrollable list of strings. This is the single-responsibility
 * principle: one component, one job. The parent controls state; this just renders.
 */
@Composable
fun WheelColumn(
    pagerState: PagerState,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    val itemHeight = 48.dp

    VerticalPager(
        state = pagerState,
        modifier = modifier.height(itemHeight * 5),
        contentPadding = PaddingValues(vertical = itemHeight * 2),
        pageSize = PageSize.Fixed(itemHeight),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) { page ->

        val offset  = pagerState.getOffsetDistanceInPages(page)
        val clamped = abs(offset).coerceAtMost(2f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .graphicsLayer {
                    alpha  = 1f - (clamped * 0.4f)
                    scaleX = 1f - (clamped * 0.15f)
                    scaleY = scaleX
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = items[page],
                fontSize   = 22.sp,
                textAlign  = TextAlign.Center,
                fontWeight = if (offset == 0f) FontWeight.Bold else FontWeight.Normal,
                color      = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

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