package dev.koenv.libraryapi.app.platform

import platform.Foundation.NSLog

actual object PlatformLog {
    actual fun d(tag: String, msg: String) { NSLog("LibraryApi/%@: %@", tag, msg) }
    actual fun e(tag: String, msg: String, throwable: Throwable?) {
        val t = throwable?.message ?: "no throwable"
        NSLog("LibraryApi/%@ ERROR: %@ | %@", tag, msg, t)
    }
}
