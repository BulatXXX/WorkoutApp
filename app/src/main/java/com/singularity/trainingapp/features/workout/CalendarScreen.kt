package com.singularity.trainingapp.features.workout

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * Workout calendar screen (Compose 2025-ready).
 *
 * Top: a compact calendar for 1/2/4 weeks with colored dots for each workout.
 * Bottom: stats for selected day. If the day has multiple workouts — swipe between them (HorizontalPager).
 */

@Immutable
enum class WorkoutKind(val color: Color) {
    Strength(Color(0xFFEF5350)),
    Cardio(Color(0xFF42A5F5)),
    Mobility(Color(0xFF66BB6A)),
    HIIT(Color(0xFFFFA726)),
    Other(Color(0xFFAB47BC));
}

@Immutable
data class ExerciseStat(
    val name: String,
    val sets: Int,
    val reps: Int?,
    val volumeKg: Int?, // Optional aggregate volume
)

@Immutable
data class WorkoutSummary(
    val id: String,
    val kind: WorkoutKind,
    val startedAt: LocalDateTime,
    val duration: Duration,
    val totalExercises: Int,
    val exerciseStats: List<ExerciseStat> = emptyList(),
)

@Immutable
data class DayPayload(
    val date: LocalDate,
    val workouts: List<WorkoutSummary> = emptyList(),
)

@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkoutCalendarScreen(
    modifier: Modifier = Modifier,
    initialRows: Int = 2, // default to 2-week view
    // Supply your data in [loadDays] – typically from ViewModel
    loadDays: (start: LocalDate, end: LocalDate) -> Map<LocalDate, DayPayload>,
    onWorkoutClick: (WorkoutSummary) -> Unit = {},
) {
    val locale = LocalContext.current.resources.configuration.locales[0] ?: Locale.getDefault()

    var rows by rememberSaveable { mutableIntStateOf(initialRows.coerceIn(1, 4)) }

    var anchorMonday by rememberSaveable {
        mutableStateOf(LocalDate.now().with(java.time.DayOfWeek.MONDAY))
    }

    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }

    // Load a sliding window for the displayed grid
    val endDate = remember(anchorMonday, rows) { anchorMonday.plusDays((rows * 7L) - 1L) }
    val days: Map<LocalDate, DayPayload> = remember(anchorMonday, endDate) {
        loadDays(anchorMonday, endDate)
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Header: month label + rows selector
        CalendarHeader(
            monthLabel = monthRangeLabel(anchorMonday, endDate, locale),
            rows = rows,
            onRowsChange = { rows = it },
            onPrev = { anchorMonday = anchorMonday.minusWeeks(rows.toLong()) },
            onNext = { anchorMonday = anchorMonday.plusWeeks(rows.toLong()) },
        )

        Spacer(Modifier.height(8.dp))

        // Calendar grid
        CalendarGrid(
            anchorMonday = anchorMonday,
            rows = rows,
            selectedDate = selectedDate,
            onSelectDate = { selectedDate = it },
            dayPayload = { date -> days[date] },
        )

        Divider(Modifier.padding(vertical = 8.dp))

        // Bottom stats for the selected day
        DayStatsPager(
            date = selectedDate,
            payload = days[selectedDate],
            onWorkoutClick = onWorkoutClick,
        )
    }
}

@Composable
private fun CalendarHeader(
    monthLabel: String,
    rows: Int,
    onRowsChange: (Int) -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = monthLabel,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StepperButton(text = "◀", onClick = onPrev)
            RowSelector(rows = rows, onRowsChange = onRowsChange)
            StepperButton(text = "▶", onClick = onNext)
        }
    }
}

@Composable
private fun StepperButton(text: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        tonalElevation = 2.dp,
        modifier = Modifier
            .size(36.dp)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
private fun RowSelector(rows: Int, onRowsChange: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(1, 2, 4).forEach { value ->
            AssistChip(
                onClick = { onRowsChange(value) },
                label = { Text("${value}w") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (rows == value) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = if (rows == value) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    anchorMonday: LocalDate,
    rows: Int,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    dayPayload: (LocalDate) -> DayPayload?,
) {
    Column(Modifier.fillMaxWidth()) {
        // Weekday labels row
        val locale = Locale.getDefault()
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (0 until 7).forEach { i ->
                val label = java.time.DayOfWeek.MONDAY.plus(i.toLong())
                    .getDisplayName(TextStyle.SHORT, locale)
                Text(
                    text = label.replaceFirstChar { it.titlecase(locale) },
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        val cellPadding = 4.dp
        val cellHeight = 52.dp

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            userScrollEnabled = false,
        ) {
            val total = rows * 7
            val allDays = (0 until total).map { anchorMonday.plusDays(it.toLong()) }
            items(allDays) { date ->
                DayCell(
                    date = date,
                    selected = date == selectedDate,
                    payload = dayPayload(date),
                    onSelect = { onSelectDate(date) },
                    cellHeight = cellHeight,
                    cellPadding = cellPadding,
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    selected: Boolean,
    payload: DayPayload?,
    onSelect: () -> Unit,
    cellHeight: Dp,
    cellPadding: Dp,
) {
    val today = LocalDate.now()
    val isToday = date == today

    val borderColor = when {
        selected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    val highlightAlpha by animateFloatAsState(
        targetValue = if (selected) 1f else if (isToday) 0.5f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "highlight"
    )

    Column(
        modifier = Modifier
            .padding(cellPadding)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable(
                role = Role.Button,
                onClick = onSelect,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(6.dp)
            .height(cellHeight),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.titleSmall,
            )
            if (isToday) {
                Text(
                    text = "•",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.alpha(0.9f)
                )
            }
        }

        // Colored dots for workouts
        val colors = payload?.workouts?.map { it.kind.color }?.take(4).orEmpty()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color),
                )
            }
            if ((payload?.workouts?.size ?: 0) > 4) {
                Text("+", style = MaterialTheme.typography.labelSmall)
            }
        }

        // subtle selected tint
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = highlightAlpha))
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DayStatsPager(
    date: LocalDate,
    payload: DayPayload?,
    onWorkoutClick: (WorkoutSummary) -> Unit,
) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM")),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )

        AnimatedContent(
            targetState = payload?.workouts.orEmpty(),
            transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "payload"
        ) { workouts ->
            if (workouts.isEmpty()) {
                EmptyDayCard()
            } else {
                val pagerState = rememberPagerState(pageCount = { workouts.size })
                val scope = rememberCoroutineScope()

                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 12.dp,
                    flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    WorkoutStatsCard(
                        workout = workouts[page],
                        onClick = { onWorkoutClick(workouts[page]) }
                    )
                }

                Spacer(Modifier.height(8.dp))
                PageIndicator(
                    count = workouts.size,
                    current = pagerState.currentPage,
                    onDotClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } }
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun EmptyDayCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Нет тренировок", style = MaterialTheme.typography.titleMedium)
            Text(
                "Запланируйте тренировку или отдохните",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WorkoutStatsCard(
    workout: WorkoutSummary,
    onClick: () -> Unit,
) {
    val started = workout.startedAt.toLocalTime().toString()
    val durationStr = formatDuration(workout.duration)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(workout.kind.color)
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = workout.kind.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.weight(1f))
                Text("Старт: $started", style = MaterialTheme.typography.labelMedium)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatChip(label = "Упражнения", value = workout.totalExercises.toString())
                StatChip(label = "Длительность", value = durationStr)
            }

            if (workout.exerciseStats.isNotEmpty()) {
                Divider()
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    workout.exerciseStats.take(6).forEach { s ->
                        ExerciseRow(stat = s)
                    }
                    if (workout.exerciseStats.size > 6) {
                        Text(
                            text = "+ ещё ${workout.exerciseStats.size - 6}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 1.dp,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
            .padding(0.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.size(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun ExerciseRow(stat: ExerciseStat) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            stat.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        val right = buildString {
            stat.reps?.let { append("$it повт.") }
            if (isNotEmpty()) append(" • ")
            stat.volumeKg?.let { append("$it кг") }
        }
        Text(
            right,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PageIndicator(count: Int, current: Int, onDotClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(count) { index ->
            val selected = index == current
            val size by animateFloatAsState(if (selected) 10f else 6f, label = "dotSize")
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                    .clickable { onDotClick(index) }
            )
        }
    }
}

private fun monthRangeLabel(start: LocalDate, end: LocalDate, locale: Locale): String {
    return if (start.month == end.month && start.year == end.year) {
        // e.g., "Апрель 2025"
        DateTimeFormatter.ofPattern("LLLL yyyy", locale).format(start)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    } else {
        // e.g., "Апр–Май 2025"
        val left = DateTimeFormatter.ofPattern("LLL", locale).format(start)
        val right = DateTimeFormatter.ofPattern("LLL yyyy", locale).format(end)
        "${left}–$right"
    }
}

private fun formatDuration(duration: Duration): String {
    val h = duration.toHours()
    val m = duration.minusHours(h).toMinutes()
    return if (h > 0) "%dh %02dm".format(h, m) else "%dm".format(m)
}

// ----------------------- PREVIEW with fake data ----------------------- //
fun fakeLoadDays(start: LocalDate, end: LocalDate): Map<LocalDate, DayPayload> {
    val map = mutableMapOf<LocalDate, DayPayload>()
    var d = start
    while (!d.isAfter(end)) {
        val workouts = when (d.dayOfMonth % 5) {
            0 -> listOf(fakeWorkout(d, WorkoutKind.Cardio))
            1 -> listOf(
                fakeWorkout(d, WorkoutKind.Strength),
                fakeWorkout(d, WorkoutKind.Mobility)
            )

            2 -> emptyList()
            3 -> listOf(fakeWorkout(d, WorkoutKind.HIIT))
            else -> listOf(
                fakeWorkout(d, WorkoutKind.Other),
                fakeWorkout(d, WorkoutKind.Strength),
                fakeWorkout(d, WorkoutKind.Cardio),
                fakeWorkout(d, WorkoutKind.Mobility)
            )
        }
        map[d] = DayPayload(d, workouts)
        d = d.plusDays(1)
    }
    return map
}


//@Preview(showBackground = true)
@Composable
private fun Preview_WorkoutCalendar() {
    val now = LocalDate.now()
    WorkoutCalendarScreen(
        initialRows = 2,
        loadDays = { start, end ->
            val map = mutableMapOf<LocalDate, DayPayload>()
            var d = start
            while (!d.isAfter(end)) {
                val workouts = when (d.dayOfMonth % 5) {
                    0 -> listOf(fakeWorkout(d, WorkoutKind.Cardio))
                    1 -> listOf(
                        fakeWorkout(d, WorkoutKind.Strength),
                        fakeWorkout(d, WorkoutKind.Mobility)
                    )

                    2 -> emptyList()
                    3 -> listOf(fakeWorkout(d, WorkoutKind.HIIT))
                    else -> listOf(
                        fakeWorkout(d, WorkoutKind.Other),
                        fakeWorkout(d, WorkoutKind.Strength),
                        fakeWorkout(d, WorkoutKind.Cardio),
                        fakeWorkout(d, WorkoutKind.Mobility)
                    )
                }
                map[d] = DayPayload(d, workouts)
                d = d.plusDays(1)
            }
            map
        }
    )
}

private fun fakeWorkout(date: LocalDate, kind: WorkoutKind): WorkoutSummary {
    return WorkoutSummary(
        id = "${date}-$kind",
        kind = kind,
        startedAt = LocalDateTime.of(date, LocalTime.of(18, 30)),
        duration = Duration.ofMinutes(listOf(35, 45, 55, 65).random().toLong()),
        totalExercises = (5..10).random(),
        exerciseStats = listOf(
            ExerciseStat("Жим лёжа", 4, 8, 3200),
            ExerciseStat("Тяга горизонтальная", 4, 10, 2800),
            ExerciseStat("Присед", 5, 5, 4500),
            ExerciseStat("Планка", 3, null, null),
            ExerciseStat("Скручивания", 3, 15, null)
        )
    )
}
