package dev.koenv.libraryapi.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Icon
import dev.koenv.libraryapi.app.ui.components.NavigationBar
import dev.koenv.libraryapi.app.ui.components.NavigationBarItem

/**
 * Compact BottomBar: icons-only, smaller height to match common mobile UI (≈56dp).
 *
 * Notes:
 * - We explicitly set a smaller height via Modifier.height(56.dp) to override the navigation
 *   bar implementation default if it uses a larger internal constant.
 * - Nav items are icons-only (label = null, alwaysShowLabel = false).
 * - Make sure your NavigationBarItem supports label = null and an `alwaysShowLabel` parameter.
 */
@Composable
fun BottomBar(current: Screen, onTabSelected: (Screen) -> Unit, modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp) // compact mobile-friendly height
            .background(AppTheme.colors.surface)
            .padding(horizontal = 0.dp),
        containerColor = AppTheme.colors.surface,
        contentColor = AppTheme.colors.onSurface,
        // keep window insets from NavigationBarDefaults, if you want to respect system bars
        windowInsets = dev.koenv.libraryapi.app.ui.components.NavigationBarDefaults.windowInsets,
    ) {
        // RowScope receiver here
        RowNavItem(icon = Icons.Filled.Home, selected = current is Screen.Home, onClick = { onTabSelected(Screen.Home) })
        RowNavItem(icon = Icons.Filled.Info, selected = current is Screen.Info, onClick = { onTabSelected(Screen.Info) })
        RowNavItem(icon = Icons.Filled.Person, selected = current is Screen.Profile, onClick = { onTabSelected(Screen.Profile) })
        RowNavItem(icon = Icons.Filled.Settings, selected = current is Screen.Settings || current.route.startsWith("settings/"), onClick = { onTabSelected(Screen.Settings) })
    }
}

/**
 * RowScope extension so it can call RowScope.NavigationBarItem directly.
 * Icons-only: label = null, alwaysShowLabel = false.
 */
@Composable
private fun RowScope.RowNavItem(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(imageVector = icon, contentDescription = null) },
        label = null,
        alwaysShowLabel = false,
    )
}