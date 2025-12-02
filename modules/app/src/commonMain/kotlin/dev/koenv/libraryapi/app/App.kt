package dev.koenv.libraryapi.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Text
import dev.koenv.libraryapi.app.ui.components.Button
import dev.koenv.libraryapi.app.ui.components.ButtonVariant
import org.jetbrains.compose.ui.tooling.preview.Preview

import dev.koenv.libraryapi.app.navigation.*
import dev.koenv.libraryapi.app.screens.*
import dev.koenv.libraryapi.app.auth.AuthManager

@Composable
@Preview
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }

    AppTheme(isDarkTheme = isDarkTheme) {
        // Auth gate
        val authed by AuthManager.isAuthenticated.collectAsState()
        if (!authed) {
            Box(
                modifier = Modifier
                    .background(AppTheme.colors.background)
                    .safeContentPadding()
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AuthScreen()
            }
            return@AppTheme
        }

        val navigator = remember { Navigator(Screen.Home) }

        Column(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    text = if (isDarkTheme) "Light" else "Dark",
                    variant = ButtonVariant.Primary,
                    onClick = { isDarkTheme = !isDarkTheme }
                )
                Button(
                    variant = ButtonVariant.PrimaryOutlined,
                    onClick = {  }
                ) {
                    Text("Other", style = AppTheme.typography.button)
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val bottomNavPadding = 64.dp

                when (val s = navigator.current) {
                    is Screen.Home -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = bottomNavPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HomeScreen()
                    }

                    is Screen.Info -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = bottomNavPadding)
                            .padding(16.dp)
                    ) { InfoScreen() }

                    is Screen.Profile -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = bottomNavPadding)
                            .padding(16.dp)
                    ) { ProfileScreen() }

                    is Screen.Settings -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = bottomNavPadding)
                            .padding(16.dp)
                    ) { SettingsScreen(navigator) }

                    is Screen.SettingsSub -> Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = bottomNavPadding)
                            .padding(16.dp)
                    ) { SettingsSubpage(s.id, navigator) }
                }
            }

            BottomBar(current = navigator.current, onTabSelected = { navigator.navigateToRoot(it) })
        }
    }
}
