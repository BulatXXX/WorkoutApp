package com.singularity.trainingapp.core.ui.scaffold


import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FabHost(cfg: FabConfig?) {
    val c = cfg ?: return
    if (!c.visible) return

    when (c.type) {
        FabType.Extended -> {
            if (c.label != null) {
                ExtendedFloatingActionButton(
                    onClick = c.onClick,
                    icon = { c.icon?.let { Icon(it, contentDescription = null) } },
                    text = { Text(c.label) }
                )
            } else {
                FloatingActionButton(onClick = c.onClick) {
                    c.icon?.let { Icon(it, contentDescription = null) }
                }
            }
        }

        FabType.Regular -> {
            FloatingActionButton(onClick = c.onClick) {
                c.icon?.let { Icon(it, contentDescription = null) }
            }
        }
    }
}