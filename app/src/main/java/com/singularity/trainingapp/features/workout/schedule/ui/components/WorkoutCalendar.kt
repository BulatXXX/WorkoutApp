package com.singularity.trainingapp.features.workout.schedule.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singularity.trainingapp.features.workout.schedule.ui.PageSlice
import com.singularity.trainingapp.features.workout.schedule.ui.ScheduleIntent
import com.singularity.trainingapp.features.workout.schedule.ui.ScheduleState
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

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
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            state.window[state.currentPage]?.title ?: LocalDate.now().month.getDisplayName(
                TextStyle.FULL_STANDALONE,
                Locale.getDefault()
            )
        )
        CalendarPager(
            modifier = Modifier.verticalSwipe(
                onSwipeUp = { onIntent(ScheduleIntent.ChangeRows(maxOf(1, state.rows / 2))) },
                onSwipeDown = { onIntent(ScheduleIntent.ChangeRows(minOf(4, state.rows * 2))) }
            ),
            pagerState = pagerState,
            isLoading = state.isLoading,
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
    isLoading: Boolean,
    window: Map<Int, PageSlice>,
    rows: Int,
    today: LocalDate,
    selectedDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
) {
    HorizontalPager(state = pagerState, modifier = modifier) { page ->
        val slice = window[page]
        CalendarPage(
            slice = slice,
            isLoading = isLoading,
            rows = rows,
            today = today,
            selectedDate = selectedDate,
            onDayClick = onDayClick
        )
    }
}

@Composable
fun Modifier.verticalSwipe(
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
    threshold: Float = 50f
): Modifier {
    var offset = 0f
    return this.draggable(
        orientation = Orientation.Vertical,
        state = rememberDraggableState { delta ->
            offset += delta
        },
        onDragStopped = {
            when {
                offset > threshold -> onSwipeDown()
                offset < -threshold -> onSwipeUp()
            }
            offset = 0f
        }
    )
}