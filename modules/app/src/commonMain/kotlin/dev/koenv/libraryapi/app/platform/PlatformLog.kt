package dev.koenv.libraryapi.app.platform

expect object PlatformLog {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String, throwable: Throwable? = null)
}
