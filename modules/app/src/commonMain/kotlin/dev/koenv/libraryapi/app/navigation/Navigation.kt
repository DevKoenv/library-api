package dev.koenv.libraryapi.app.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * Minimal navigator: stack-based, only what we need for push/pop and tab resets.
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Info : Screen("info")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    data class SettingsSub(val id: String) : Screen("settings/$id")
}

class Navigator(initial: Screen = Screen.Home) {
    private val _stack: SnapshotStateList<Screen> = mutableStateListOf(initial)
    val stack: SnapshotStateList<Screen> get() = _stack

    val current: Screen
        get() = _stack.last()

    fun push(screen: Screen) {
        _stack.add(screen)
    }

    fun pop() {
        if (_stack.size > 1) _stack.removeAt(_stack.lastIndex)
    }

    fun replace(screen: Screen) {
        if (_stack.isNotEmpty()) {
            _stack[_stack.lastIndex] = screen
        } else {
            _stack.add(screen)
        }
    }

    fun navigateToRoot(screen: Screen) {
        _stack.clear()
        _stack.add(screen)
    }
}