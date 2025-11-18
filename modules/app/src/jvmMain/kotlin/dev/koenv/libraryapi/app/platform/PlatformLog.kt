package dev.koenv.libraryapi.app.platform

actual object PlatformLog {
    actual fun d(tag: String, msg: String) { println("LibraryApi/$tag: $msg") }
    actual fun e(tag: String, msg: String, throwable: Throwable?) {
        System.err.println("LibraryApi/$tag ERROR: $msg" + (throwable?.let { " | ${it.message}" } ?: ""))
        throwable?.printStackTrace()
    }
}
