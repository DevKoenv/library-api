package dev.koenv.libraryapi.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Text

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text("Profile", style = AppTheme.typography.h2)
        Text("This is the Profile page. Add user profile content here.", style = AppTheme.typography.body1)
    }
}