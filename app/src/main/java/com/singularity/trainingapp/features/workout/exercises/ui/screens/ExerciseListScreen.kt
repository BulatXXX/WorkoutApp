package com.singularity.trainingapp.features.workout.exercises.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularity.trainingapp.features.workout.data.local.entities.Exercise
import com.singularity.trainingapp.features.workout.exercises.ui.viewmodel.ExerciseListViewModel
import com.singularity.trainingapp.features.workout.exercises.ui.viewmodel.ExercisesListState

@Composable
fun ExercisesListScreen(exerciseListViewModel: ExerciseListViewModel) {
    val state = exerciseListViewModel.uiState.collectAsStateWithLifecycle()
    ExerciseListScreenContent(state.value)
}

@Composable
fun ExerciseListScreenContent(exercisesListState: ExercisesListState) {
    LazyColumn {
        items(exercisesListState.exerciseList) { exercise ->
            ExerciseCard(exercise)
        }
    }
}


@Composable
fun ExerciseCard(exercise: Exercise) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val container = remember(
        exercise.hasRepeats,
        exercise.hasWeight,
        exercise.hasDistance,
        exercise.hasDuration
    ) {
        flagsToColor(
            hasRepeats = exercise.hasRepeats,
            hasWeight = exercise.hasWeight,
            hasDistance = exercise.hasDistance,
            hasDuration = exercise.hasDuration
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = container)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = exercise.name, style = MaterialTheme.typography.titleMedium)

            AnimatedVisibility(visible = expanded) {
                Column(Modifier.padding(top = 8.dp)) {
                    if (!exercise.description.isNullOrBlank()) {
                        Text(text = exercise.description)
                        Spacer(Modifier.height(8.dp))
                    }

                    val params = listOf(
                        "Повторы" to exercise.hasRepeats,
                        "Вес" to exercise.hasWeight,
                        "Дистанция" to exercise.hasDistance,
                        "Длительность" to exercise.hasDuration,
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                    ) {
                        items(params) { (label, enabled) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text(text = if (enabled) "✅" else "❌")
                                Spacer(Modifier.width(6.dp))
                                Text(text = label)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Маппим комбинацию флагов в цвет.
 * Битовое представление: R W D T  (R=Repeats, W=Weight, D=Distance, T=Duration)
 * Пример: R=true,W=false,D=true,T=false → 1010b = 10 → colors[10]
 */
private fun flagsToColor(
    hasRepeats: Boolean,
    hasWeight: Boolean,
    hasDistance: Boolean,
    hasDuration: Boolean
): Color {
    val id =
        ((if (hasRepeats) 1 else 0) shl 3) or
                ((if (hasWeight) 1 else 0) shl 2) or
                ((if (hasDistance) 1 else 0) shl 1) or
                (if (hasDuration) 1 else 0)


    // Палитра из 16 оттенков (достаточно контрастная, но мягкая для фона)
    // Можно заменить на свою бренд-палитру при желании.
    val colors = listOf(
        Color(0xFF9E9E9E), // 0000 — ничего (серый)
        Color(0xFF2196F3), // 0001 — T (синий)
        Color(0xFF4CAF50), // 0010 — D (зелёный)
        Color(0xFF009688), // 0011 — D+T (бирюзовый)
        Color(0xFFFF9800), // 0100 — W (оранжевый)
        Color(0xFFFF5722), // 0101 — W+T (оранжево-красный)
        Color(0xFFFFC107), // 0110 — W+D (янтарь)
        Color(0xFFFFEB3B), // 0111 — W+D+T (жёлтый)
        Color(0xFF9C27B0), // 1000 — R (фиолетовый)
        Color(0xFF673AB7), // 1001 — R+T (тёмно-фиолетовый)
        Color(0xFF3F51B5), // 1010 — R+D (индиго)
        Color(0xFF00BCD4), // 1011 — R+D+T (голубой)
        Color(0xFFE91E63), // 1100 — R+W (розовый)
        Color(0xFFF44336), // 1101 — R+W+T (красный)
        Color(0xFF795548), // 1110 — R+W+D (коричневый)
        Color(0xFF000000)  // 1111 — R+W+D+T (чёрный)
    )

    return colors[id.coerceIn(0, colors.lastIndex)]
}