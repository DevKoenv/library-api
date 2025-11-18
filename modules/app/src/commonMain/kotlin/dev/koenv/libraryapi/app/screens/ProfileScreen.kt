package dev.koenv.libraryapi.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.koenv.libraryapi.app.auth.AuthManager
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Text

@Composable
fun ProfileScreen() {
    val user by AuthManager.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Profile", style = AppTheme.typography.h2)

        if (user == null) {
            Text("No user.", style = AppTheme.typography.body1)
            return
        }

        Text("ID: ${user!!.id}", style = AppTheme.typography.body1)
        Text("Email: ${user!!.email}", style = AppTheme.typography.body1)
        Text("Role: ${user!!.role}", style = AppTheme.typography.body1)

        Text("", style = AppTheme.typography.body1) // spacer by empty line
        Text("Note: demo data is in-memory only.", style = AppTheme.typography.body2)
    }
}
