package com.singularity.trainingapp.features.workout.exercises.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.singularity.trainingapp.core.ui.scaffold.BottomBarConfig
import com.singularity.trainingapp.core.ui.scaffold.LocalScaffoldStateController
import com.singularity.trainingapp.core.ui.scaffold.NavIcon
import com.singularity.trainingapp.core.ui.scaffold.NavIconType
import com.singularity.trainingapp.core.ui.scaffold.TopBarAction
import com.singularity.trainingapp.core.ui.scaffold.TopBarConfig
import com.singularity.trainingapp.core.ui.scaffold.TopBarStyle
import com.singularity.trainingapp.features.workout.exercises.ui.viewmodel.ExerciseCreateIntent
import com.singularity.trainingapp.features.workout.exercises.ui.viewmodel.ExerciseCreateState
import com.singularity.trainingapp.features.workout.exercises.ui.viewmodel.ExerciseCreateViewModel

@Composable
fun ExerciseCreateScreen(
    exerciseCreateViewModel: ExerciseCreateViewModel,
    onBack: () -> Unit = {}
) {
    val state by exerciseCreateViewModel.uiState.collectAsStateWithLifecycle()
    val scaffold = LocalScaffoldStateController.current

    LaunchedEffect(Unit) {
        scaffold.updateScaffold(
            topBarConfig = TopBarConfig(
                title = "Новое упражнение",
                style = TopBarStyle.Medium,
                navigation = NavIcon(NavIconType.Back, onBack),
                actions = listOf(
                    TopBarAction(
                        id = "save",
                        icon = Icons.Default.Check,
                        onClick = {
                            exerciseCreateViewModel.sendIntent(ExerciseCreateIntent.SaveExercise)
                            onBack()
                        }
                    )
                )
            ),
            fabConfig = null,
            bottomBarConfig = BottomBarConfig(visible = false)
        )
    }

    ExerciseCreateContent(
        state = state,
        onNameChange = { exerciseCreateViewModel.sendIntent(ExerciseCreateIntent.NameChanged(it)) },
        onDescriptionChange = {
            exerciseCreateViewModel.sendIntent(
                ExerciseCreateIntent.DescriptionChanged(
                    it
                )
            )
        },
        onToggleRepeats = { exerciseCreateViewModel.sendIntent(ExerciseCreateIntent.ToggleHasRepeats) },
        onToggleDistance = { exerciseCreateViewModel.sendIntent(ExerciseCreateIntent.ToggleHasDistance) },
        onToggleWeight = { exerciseCreateViewModel.sendIntent(ExerciseCreateIntent.ToggleHasWeight) },
    )
}

@Composable
private fun ExerciseCreateContent(
    state: ExerciseCreateState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onToggleRepeats: () -> Unit,
    onToggleDistance: () -> Unit,
    onToggleWeight: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.exerciseName,
            onValueChange = onNameChange,
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.exerciseDescription,
            onValueChange = onDescriptionChange,
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Параметры", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.hasRepeats, onCheckedChange = { onToggleRepeats() })
            Spacer(Modifier.width(4.dp))
            Text("Повторы")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.hasWeight, onCheckedChange = { onToggleWeight() })
            Spacer(Modifier.width(4.dp))
            Text("Вес")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.hasDistance, onCheckedChange = { onToggleDistance() })
            Spacer(Modifier.width(4.dp))
            Text("Дистанция")
        }

        Spacer(Modifier.weight(1f))

    }
}