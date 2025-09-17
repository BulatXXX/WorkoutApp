package com.singularity.trainingapp.core.ui.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector


@Immutable
data class TopBarConfig(
    val style: TopBarStyle = TopBarStyle.Small,
    val title: String = "",
    val navigation: NavIcon? = null,
    val actions: List<TopBarAction> = emptyList(),
    val search: SearchConfig? = null,
    val menu: MenuConfig? = null,
    val contextual: ContextualBar? = null,
    val scroll: TopBarScroll = TopBarScroll.None
)

enum class TopBarStyle { Small, Medium, Large, Search }

@Immutable
data class NavIcon(
    val type: NavIconType,
    val onClick: () -> Unit
)

enum class NavIconType { Back, Close, Drawer }

@Composable
fun NavIconType.asImageVector(): ImageVector = when (this) {
    NavIconType.Back -> Icons.AutoMirrored.Filled.ArrowBack
    NavIconType.Close -> Icons.Default.Close
    NavIconType.Drawer -> Icons.Default.Menu
}

@Immutable
data class TopBarAction(
    val id: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Immutable
data class MenuConfig(
    val items: List<MenuItem>
)

@Immutable
data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector? = null,
    val destructive: Boolean = false,
    val onClick: () -> Unit
)

@Immutable
data class ContextualBar(
    val title: String,
    val actions: List<TopBarAction> = emptyList()
)

enum class TopBarScroll { None, EnterAlways, ExitUntilCollapsed, Pinned }


@Immutable
data class SearchConfig(
    val mode: SearchMode = SearchMode.Icon,
    val query: String = "",
    val onQueryChange: (String) -> Unit,
    val onSubmit: (() -> Unit)? = null,
    val onClear: (() -> Unit)? = null,
    val placeholder: String = "Поиск…",
    val expanded: Boolean = mode == SearchMode.Field
)

enum class SearchMode { Icon, Field }


@Immutable
data class FabConfig(
    val visible: Boolean = false,
    val icon: ImageVector? = null,
    val label: String? = null,
    val type: FabType = FabType.Regular,
    val onClick: () -> Unit = {}
)

enum class FabType { Regular, Extended }


@Immutable
data class BottomBarConfig(
    val visible: Boolean = true
)

@Stable
class ScaffoldStateController {
    var topBar: TopBarConfig? by mutableStateOf(null)
    var fab: FabConfig? by mutableStateOf(null)
    var bottomBar: BottomBarConfig? by mutableStateOf(BottomBarConfig())

    fun updateScaffold(
        topBarConfig: TopBarConfig? = null,
        fabConfig: FabConfig? = null,
        bottomBarConfig: BottomBarConfig? = BottomBarConfig()
    ) {
        topBar = topBarConfig
        fab = fabConfig
        bottomBar = bottomBarConfig
    }

}

val LocalScaffoldStateController = staticCompositionLocalOf<ScaffoldStateController> {
    error("No ScaffoldStateController provided")
}