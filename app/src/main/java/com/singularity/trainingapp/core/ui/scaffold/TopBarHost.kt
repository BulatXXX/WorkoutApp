@file:OptIn(ExperimentalMaterial3Api::class)

package com.singularity.trainingapp.core.ui.scaffold

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction


@Composable
fun TopBarHost(cfg: TopBarConfig?) {
    if (cfg == null) return

    when (cfg.style) {
        TopBarStyle.Small -> TopAppBar(
            title = { Text(cfg.title) },
            navigationIcon = {
                cfg.navigation?.let { nav ->
                    IconButton(onClick = nav.onClick) {
                        Icon(imageVector = nav.type.asImageVector(), contentDescription = null)
                    }
                }
            },
            actions = {
                cfg.actions.forEach { a ->
                    IconButton(onClick = a.onClick) { Icon(a.icon, contentDescription = null) }
                }
            }
        )

        TopBarStyle.Medium -> MediumTopAppBar(
            title = { Text(cfg.title) },
            navigationIcon = {
                cfg.navigation?.let { nav ->
                    IconButton(onClick = nav.onClick) {
                        Icon(nav.type.asImageVector(), contentDescription = null)
                    }
                }
            },
            actions = {
                cfg.actions.forEach { a ->
                    IconButton(onClick = a.onClick) { Icon(a.icon, contentDescription = null) }
                }
            }
        )

        TopBarStyle.Large -> LargeTopAppBar(
            title = { Text(cfg.title) },
            navigationIcon = {
                cfg.navigation?.let { nav ->
                    IconButton(onClick = nav.onClick) {
                        Icon(nav.type.asImageVector(), contentDescription = null)
                    }
                }
            },
            actions = {
                cfg.actions.forEach { a ->
                    IconButton(onClick = a.onClick) { Icon(a.icon, contentDescription = null) }
                }
            }
        )

        TopBarStyle.Search -> TopAppBar(
            title = {
                val s = requireNotNull(cfg.search) { "SearchConfig required for style=Search" }
                OutlinedTextField(
                    value = s.query,
                    onValueChange = s.onQueryChange,
                    singleLine = true,
                    placeholder = { Text(s.placeholder) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { /* hide IME if needed */ }),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                cfg.navigation?.let { nav ->
                    IconButton(onClick = nav.onClick) {
                        Icon(nav.type.asImageVector(), contentDescription = null)
                    }
                }
            },
            actions = {
                cfg.actions.forEach { a ->
                    IconButton(onClick = a.onClick) { Icon(a.icon, contentDescription = null) }
                }
            }
        )
    }
}
