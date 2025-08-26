package com.singularity.trainingapp.features.workout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutListScreen(onItemClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(WorkoutUi.items) { item ->
            ListItem(
                headlineContent = { Text(item.title) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(item.title) }
                    .padding(horizontal = 16.dp)
            )
            Divider()
        }
    }
}

@Immutable
data class WorkoutUi(
    val id: String,
    val title: String,
) {
    companion object {
        val items: List<WorkoutUi> = listOf(WorkoutUi("1id","Good"), WorkoutUi("2id","Bad"),WorkoutUi("3id","OK"))
    }
}
