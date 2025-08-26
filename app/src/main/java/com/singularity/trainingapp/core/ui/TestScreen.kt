package com.singularity.trainingapp.core.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TestScreen(name: String, modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "rainbowHue")
    val hue by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hue"
    )

    val color = Color.hsv(hue, 1f, 1f)

    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "$name \ncurrently unavailable",
            style = TextStyle(fontSize = 32.sp, color = color, textAlign = TextAlign.Center),
        )
    }
}