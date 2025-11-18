package dev.koenv.libraryapi.app.auth

import dev.koenv.libraryapi.app.platform.PlatformLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AuthManager {
    private const val TAG = "AuthManager"

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(email: String, password: String) {
        PlatformLog.d(TAG, "login() called (dummy api)")

        // TODO: Replace with API call to backend
        val u = AuthRepository.tryLogin(email, password)

        if (u != null) {
            _currentUser.value = u
            _isAuthenticated.value = true
        } else {
            // keep unauthenticated; UI can show simple hint if needed
        }
    }

    fun register(email: String, password: String) {
        PlatformLog.d(TAG, "register() called (dummy api)")

        // TODO: Replace with API call to backend
        val res = AuthRepository.tryRegister(email, password)

        res.onSuccess { u ->
            _currentUser.value = u
            _isAuthenticated.value = true
        }.onFailure { e ->
            PlatformLog.e(TAG, "register() failed", e)
        }
    }

    fun logout() {
        // TODO: Add API call to backend
        PlatformLog.d(TAG, "logout() called (dummy api)")
        _currentUser.value = null
        _isAuthenticated.value = false
    }
}
