package com.singularity.trainingapp.features.workout.schedule.ui.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleIntent
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleState
import com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.utils.getMonth
import com.singularity.trainingapp.features.workout.schedule.ui.components.calendar.utils.pageStart
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
    val start = state.baseAnchorMonday

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page -> onIntent(ScheduleIntent.PageChanged(page)) }
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
            Text(
                modifier = Modifier.width(200.dp),
                text = getMonth(start, state.currentPage, state.rows)
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onIntent(ScheduleIntent.ChangeRows(1)) }) { Text("1") }
                TextButton(onClick = { onIntent(ScheduleIntent.ChangeRows(2)) }) { Text("2") }
                TextButton(onClick = { onIntent(ScheduleIntent.ChangeRows(4)) }) { Text("4") }
            }
        }

        CalendarPager(
            pagerState = pagerState,
            currentDate = state.currentDate,
            selectedDate = state.selectedDate,
            onItemClicked = { onIntent(ScheduleIntent.SelectDate(it)) },
            baseAnchorMonday = start,
            rows = state.rows,
        )
    }

}

@Composable
fun CalendarPager(
    pagerState: PagerState,
    rows: Int,
    baseAnchorMonday: LocalDate,
    currentDate: LocalDate,
    selectedDate: LocalDate,
    onItemClicked: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(state = pagerState, modifier = modifier) { page ->

        val start = remember(page, rows, baseAnchorMonday) {
            pageStart(baseAnchorMonday, page, rows)
        }
        val dates = remember(start, rows) {
            List(rows * 7) { i -> start.plusDays(i.toLong()) }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            items(7) { index ->
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                ) {
                    Text(days[index])
                }
            }
            items(dates.size) { index ->
                val date = dates[index]
                TextButton(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            when (date) {
                                selectedDate -> Color.Blue
                                currentDate -> Color.Black
                                else -> Color.DarkGray
                            }
                        ),
                    onClick = { onItemClicked(date) }
                ) {
                    Text(date.dayOfMonth.toString())
                }
            }
        }
    }
}


val days = listOf(
    "Mon",
    "Tue",
    "Wed",
    "Thu",
    "Fri",
    "Sat",
    "Sun",
)

