package com.singularity.trainingapp.features.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.padding(50.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(color = Color.Red),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Name", style = TextStyle(
                        fontSize = 24.sp
                    )
                )
                Text(
                    "Nikita", style = TextStyle(
                        fontSize = 24.sp
                    )
                )

            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(color = Color.Green),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Registration Date", style = TextStyle(
                        fontSize = 24.sp
                    )
                )
                Text(
                    "19.08.2004", style = TextStyle(
                        fontSize = 24.sp
                    )
                )

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}