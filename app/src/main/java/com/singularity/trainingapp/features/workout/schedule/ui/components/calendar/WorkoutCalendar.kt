package com.singularity.trainingapp.features.workout.schedule.ui.components.calendar


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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.utils.days
import com.singularity.trainingapp.features.workout.schedule.ui.state.PageSlice
import com.singularity.trainingapp.features.workout.schedule.ui.state.ScheduleIntent
import com.singularity.trainingapp.features.workout.schedule.ui.state.ScheduleState
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate

@Composable
fun WorkoutCalendar(
    modifier: Modifier = Modifier,
    state: ScheduleState,
    onIntent: (ScheduleIntent) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = ScheduleState.BASE_PAGE,
        pageCount = { ScheduleState.PAGE_COUNT }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page -> onIntent(ScheduleIntent.PageChanged(page)) }
    }

    LaunchedEffect(state.currentPage) {
        if (pagerState.currentPage != state.currentPage) {
            pagerState.scrollToPage(state.currentPage)
        }
    }


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(state.window[state.currentPage]?.title ?: "No Data")
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                listOf(1, 2, 4).forEach { r ->
                    TextButton(
                        onClick = { if (state.rows != r) onIntent(ScheduleIntent.ChangeRows(r)) },
                        enabled = state.rows != r
                    ) { Text("$r") }
                }

            }

        }
        CalendarPager(
            modifier = Modifier,
            pagerState = pagerState,
            window = state.window,
            rows = state.rows,
            today = state.today,
            selectedDate = state.selectedDate,
        ) {
            onIntent(ScheduleIntent.SelectDate(it))
        }
    }

}

@Composable
fun CalendarPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    window: Map<Int, PageSlice>,
    rows: Int,
    today: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
) {
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        val slice = window[page]
        CalendarPage(
            slice = slice,               // null -> плейсхолдер/шиммер
            rows = rows,
            today = today,
            selectedDate = selectedDate,
            onDayClick = onDayClick
        )
    }
}

private val WeekdayHeaderHeight: Dp = 24.dp
private val DayBadgeHeight: Dp = 40.dp
private val DayCellHeight: Dp = 64.dp


@Composable
fun CalendarPage(
    slice: PageSlice?,
    rows: Int,
    today: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    if (slice == null) {
        Text("Loading…")
        return
    }

    val dates = slice.dates.days()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(weekdays) { day ->
            Box(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .height(WeekdayHeaderHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(text = day, textAlign = TextAlign.Center)
            }
        }
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
                    .clickable { onDayClick(date) }
            ) {
                val bg = when {
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
                            .background(bg),
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

val weekdays = listOf(
    "Mon",
    "Tue",
    "Wed",
    "Thu",
    "Fri",
    "Sat",
    "Sun",
)

