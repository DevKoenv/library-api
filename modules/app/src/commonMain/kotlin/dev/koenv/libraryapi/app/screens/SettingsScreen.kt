package dev.koenv.libraryapi.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koenv.libraryapi.app.navigation.Navigator
import dev.koenv.libraryapi.app.navigation.Screen
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Button
import dev.koenv.libraryapi.app.ui.components.ButtonVariant
import dev.koenv.libraryapi.app.ui.components.Text
import dev.koenv.libraryapi.app.auth.AuthManager

@Composable
fun SettingsScreen(navigator: Navigator) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings", style = AppTheme.typography.h2)
        Text("Session controls and demo navigation.", style = AppTheme.typography.body1)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(variant = ButtonVariant.Primary, onClick = { navigator.push(Screen.SettingsSub("account")) }) {
                Text("Account", style = AppTheme.typography.button)
            }
            Button(variant = ButtonVariant.PrimaryOutlined, onClick = { navigator.push(Screen.SettingsSub("appearance")) }) {
                Text("Appearance", style = AppTheme.typography.button)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Other settings content goes here.", style = AppTheme.typography.body1)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            text = "Logout",
            variant = ButtonVariant.Destructive,
            onClick = { AuthManager.logout() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SettingsSubpage(id: String, navigator: Navigator) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Settings - $id", style = AppTheme.typography.h2)
        Text("Details for $id", style = AppTheme.typography.body1)
        Button(onClick = { navigator.pop() }) {
            Text("Back", style = AppTheme.typography.button)
        }
    }
}
