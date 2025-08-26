package com.singularity.trainingapp.core.ui


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.singularity.trainingapp.R
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.singularity.trainingapp.core.navigation.TabChat
import com.singularity.trainingapp.core.navigation.TabFeed
import com.singularity.trainingapp.core.navigation.TabProfile
import com.singularity.trainingapp.core.navigation.TabRoute
import com.singularity.trainingapp.core.navigation.TabWorkout
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun TrainingBottomBarHost(
    nav: NavHostController,
    tabs: List<BottomTab> = bottomTabs,
) {
    val backEntry by nav.currentBackStackEntryAsState()
    val dest: NavDestination? = backEntry?.destination

    val selectedTab: TabRoute? = when {
        dest.isInTab(TabWorkout) -> TabWorkout
        dest.isInTab(TabFeed) -> TabFeed
        dest.isInTab(TabChat) -> TabChat
        dest.isInTab(TabProfile) -> TabProfile
        else -> null
    }

    if (selectedTab != null) {
        TrainingBottomBar(
            tabs = tabs,
            selected = selectedTab,
            onSelect = { tab ->
                if (tab == selectedTab) {
                    nav.popToTabRoot(tab)
                } else {
                    nav.navigate(tab) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}

private fun NavDestination?.isInTab(tab: TabRoute): Boolean {
    val tabRoute = tab::class.qualifiedName
    if (this == null || tabRoute == null) return false
    return hierarchy.any { it.route == tabRoute }
}

private fun NavHostController.popToTabRoot(tab: TabRoute) {
    val tabRoute = tab::class.qualifiedName ?: return
    val tabNodeId = graph.findNode(tabRoute)?.id
    if (tabNodeId != null) {
        navigate(tab) {
            popUpTo(tabNodeId) {
                inclusive = false
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    } else {
        navigate(tab) {
            popUpTo(graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
fun TrainingBottomBar(
    tabs: List<BottomTab>,
    selected: TabRoute,
    onSelect: (TabRoute) -> Unit,
) {
    NavigationBar {
        tabs.forEach { tab ->
            val isSelected = (selected::class == tab.route::class)
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(tab.route) },
                icon = {
                    Icon(
                        ImageVector.vectorResource(tab.iconRes),
                        contentDescription = stringResource(tab.title)
                    )
                },
                label = { Text(stringResource(tab.title)) },
                alwaysShowLabel = true
            )
        }
    }
}

sealed class BottomTab(
    val route: TabRoute,
    @StringRes val title: Int,
    @DrawableRes val iconRes: Int,
) {
    data object Training :
        BottomTab(TabWorkout, R.string.workout_tab_name, R.drawable.fitness_center_24px)

    data object Feed :
        BottomTab(TabFeed, R.string.feed_tab_name, R.drawable.news_24px)

    data object Chat :
        BottomTab(TabChat, R.string.chat_tab_name, R.drawable.chat_24px)

    data object Profile :
        BottomTab(TabProfile, R.string.profile_tab_name, R.drawable.account_circle_24px)
}

val bottomTabs = listOf(
    BottomTab.Training,
    BottomTab.Feed,
    BottomTab.Chat,
    BottomTab.Profile
)