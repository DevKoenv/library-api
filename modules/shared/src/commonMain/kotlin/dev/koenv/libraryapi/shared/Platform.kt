package dev.koenv.libraryapi.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform