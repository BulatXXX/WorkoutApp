package com.singularity.trainingapp.features.workout.schedule.ui.components.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.singularity.trainingapp.features.workout.schedule.data.DayMetadata
import com.singularity.trainingapp.features.workout.schedule.data.DaysRange
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleIntent
import com.singularity.trainingapp.features.workout.schedule.data.ScheduleState
import com.singularity.trainingapp.features.workout.schedule.data.days
import java.time.DayOfWeek
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
//            Text(
//                modifier = Modifier.width(200.dp),
//                text = getMonth(start, state.currentPage, state.rows)
//            )
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
            rows = state.rows,
            selectedDate = state.selectedDate,
            today = state.today,
            window = state.window,
            onDayClicked = { onIntent(ScheduleIntent.SelectDate(it)) },
            anchorMonday = LocalDate.now().with(DayOfWeek.MONDAY),
        )
    }

}

@Composable
fun CalendarPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    rows: Int,
    selectedDate: LocalDate,
    today: LocalDate,
    window: Map<DaysRange, Map<LocalDate, DayMetadata>>,
    onDayClicked: (LocalDate) -> Unit,
    anchorMonday: LocalDate,
) {
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        val range = remember(rows, page, anchorMonday) {
            computePageRange(anchorMonday, page, rows)
        }
        Log.d("PAGER", "${range.daysCount}")
        Log.d("PAGER", "${window.size}")
        val metaByDate = window[range]

        val dates = range.days()
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
            items(dates.size, key = { i -> dates[i].toEpochDay() }) { i ->
                val date = dates[i]
                //val dots = metaByDate[date]?.dots.orEmpty()
                TextButton(
                    modifier = Modifier
                        .padding(6.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            when (date) {
                                selectedDate -> Color.Blue
                                today -> Color.Black
                                else -> Color.DarkGray
                            }
                        ),
                    onClick = {
                        onDayClicked(date)
                    }
                ) {
                    Text(date.dayOfMonth.toString())
                }
            }

        }
    }
}

fun computePageRange(anchorMonday: LocalDate, page: Int, rows: Int): DaysRange {
    val delta = page - ScheduleState.BASE_PAGE
    val start = anchorMonday.plusWeeks(delta.toLong() * rows)
    val endExclusive = start.plusDays((rows * 7).toLong())
    return DaysRange(start, endExclusive)
}

/*

*/
val days = listOf(
    "Mon",
    "Tue",
    "Wed",
    "Thu",
    "Fri",
    "Sat",
    "Sun",
)

