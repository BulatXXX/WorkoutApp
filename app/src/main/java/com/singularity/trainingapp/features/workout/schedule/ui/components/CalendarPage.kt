package com.singularity.trainingapp.features.workout.schedule.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.singularity.trainingapp.features.workout.schedule.ui.PageSlice
import com.singularity.trainingapp.features.workout.schedule.utils.days
import com.singularity.trainingapp.features.workout.schedule.utils.localizedWeekdays
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import java.time.LocalDate

private val WeekdayHeaderHeight: Dp = 24.dp
private val DayBadgeHeight: Dp = 40.dp
private val DayCellHeight: Dp = 64.dp

@Composable
fun CalendarPage(
    slice: PageSlice?,
    isLoading: Boolean,
    rows: Int,
    today: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
) {
    Column {
        WeekdayHeader()
        if (slice == null || isLoading == true) {
            DayShimmers(rows)
        } else {
            DayGrid(
                slice = slice,
                today = today,
                selectedDate = selectedDate,
                onDayClick = onDayClick
            )
        }
    }
}

@Composable
fun DayShimmers(rows: Int) {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(rows * 7) { day ->
            Box(
                modifier = Modifier
                    .height(DayCellHeight)
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(DayBadgeHeight)
                            .clip(CircleShape)
                            .shimmer(shimmer)
                            .background(Color.Gray.copy(alpha = 0.25f))
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekdayHeader() {
    val weekdays = remember { localizedWeekdays() }
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(weekdays) { day ->
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .height(WeekdayHeaderHeight),
                contentAlignment = Alignment.Center
            ) { Text(day, textAlign = TextAlign.Center) }
        }
    }
}

@Composable
private fun DayGrid(
    slice: PageSlice,
    today: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    val dates = slice.dates.days()
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(dates) { date ->
            val isSelected = date == selectedDate
            val isToday = date == today
            val meta = slice.dots[date]

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(DayCellHeight)
                    .padding(vertical = 6.dp, horizontal = 0.dp)
            ) {
                val backgroundColor = when {
                    isSelected -> Color(0xFF2962FF)
                    isToday -> Color(0xFF263238)
                    else -> Color.Transparent
                }
                Box(
                    modifier = Modifier
                        .size(DayBadgeHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(DayBadgeHeight)
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .clickable { onDayClick(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            modifier = Modifier
                                .padding(horizontal = 10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    meta?.dots?.take(4)?.forEach { dot ->
                        Canvas(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .padding(0.dp)
                                .size(6.dp)
                        ) {
                            drawCircle(color = Color(dot.color))
                        }
                    }
                }
            }
        }
    }
}