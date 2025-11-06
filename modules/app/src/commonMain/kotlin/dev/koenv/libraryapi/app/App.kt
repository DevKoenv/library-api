package dev.koenv.libraryapi.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Text
import dev.koenv.libraryapi.app.ui.components.Button
import dev.koenv.libraryapi.app.ui.components.ButtonVariant
import dev.koenv.libraryapi.shared.Greeting
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import libraryapi.modules.app.generated.resources.Res
import libraryapi.modules.app.generated.resources.compose_multiplatform

import dev.koenv.libraryapi.app.navigation.*
import dev.koenv.libraryapi.app.screens.*

/**
 * AppWithNav: layout changes to ensure:
 * - Bottom bar is full width and content doesn't get hidden behind it.
 * - Content area is scrollable where necessary.
 *
 * Approach:
 * - Column with three major areas:
 *   1) Top controls (small)
 *   2) Content area (weight = 1f) — put per-screen content here; screens should use Column verticalScroll if they need scrolling.
 *   3) BottomBar (always visible)
 *
 * - To avoid content being overlapped by the BottomBar, screens should add bottom padding equal to the NavigationBar height.
 *   We use NavigationBarDefaults.NavigationBarHeight for that. If your NavigationBarDefaults is internal, set the same height constant here (80.dp).
 */
@Composable
@Preview
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }

    AppTheme(isDarkTheme = isDarkTheme) {
        val navigator = remember { Navigator(Screen.Home) }

        Column(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top row: your existing buttons (use smaller vertical spacing)
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
                    onClick = {}
                ) {
                    Text("Other", style = AppTheme.typography.button)
                }
            }

            // Image + greeting area (small)
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(Res.drawable.compose_multiplatform), contentDescription = null)
                Text(text = "Compose: $greeting", style = AppTheme.typography.h1)
            }

            // Main content area: weight=1 so it takes available space. Use internal scrolling where necessary.
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                // reduced bottom padding to reflect the smaller nav bar height (56dp + breathing = 64dp)
                val bottomNavPadding = 64.dp

                when (val s = navigator.current) {
                    is Screen.Home -> Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = bottomNavPadding)
                        .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HomeScreen()
                    }
                    is Screen.Info -> Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = bottomNavPadding)
                        .padding(16.dp)) {
                        InfoScreen()
                    }
                    is Screen.Profile -> Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = bottomNavPadding)
                        .padding(16.dp)) {
                        ProfileScreen()
                    }
                    is Screen.Settings -> Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = bottomNavPadding)
                        .padding(16.dp)) {
                        SettingsScreen(navigator)
                    }
                    is Screen.SettingsSub -> Column(modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = bottomNavPadding)
                        .padding(16.dp)) {
                        SettingsSubpage(s.id, navigator)
                    }
                }
            }

            // Bottom bar: compact icons-only bar
            BottomBar(current = navigator.current, onTabSelected = { navigator.navigateToRoot(it) })
        }
    }
}