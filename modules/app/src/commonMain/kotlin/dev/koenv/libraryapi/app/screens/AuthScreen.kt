package dev.koenv.libraryapi.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.koenv.libraryapi.app.auth.AuthManager
import dev.koenv.libraryapi.app.ui.AppTheme
import dev.koenv.libraryapi.app.ui.components.Button
import dev.koenv.libraryapi.app.ui.components.ButtonVariant
import dev.koenv.libraryapi.app.ui.components.Text
import dev.koenv.libraryapi.app.ui.components.textfield.TextField
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AuthScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf(Mode.Login) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (mode == Mode.Login) "Login" else "Register",
            style = AppTheme.typography.h2
        )
        Text("Dummy auth. Email/password accepted if it matches a stored user. Register adds an in-memory user.", style = AppTheme.typography.body2)

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = !error.isNullOrEmpty()
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = !error.isNullOrEmpty()
        )

        if (error != null) {
            Text(error!!, style = AppTheme.typography.body3)
        }

        Spacer(Modifier.height(8.dp))

        Button(
            text = if (mode == Mode.Login) "Login" else "Register",
            variant = ButtonVariant.Primary,
            onClick = {
                error = null
                if (mode == Mode.Login) {
                    AuthManager.login(email, password)
                    // simple hint when login fails
                    if (!AuthManager.isAuthenticated.value) error = "Invalid credentials for demo user."
                } else {
                    AuthManager.register(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            text = if (mode == Mode.Login) "Switch to Register" else "Switch to Login",
            variant = ButtonVariant.SecondaryOutlined,
            onClick = { mode = if (mode == Mode.Login) Mode.Register else Mode.Login },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Text("Seed account: admin@library.local / demo", style = AppTheme.typography.body3)
        Text("Seed account: user@library.local / demo", style = AppTheme.typography.body3)
    }
}

private enum class Mode { Login, Register }
