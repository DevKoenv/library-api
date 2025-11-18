package dev.koenv.libraryapi.app.platform

import android.util.Log

actual object PlatformLog {
    private const val GLOBAL = "LibraryApi"
    actual fun d(tag: String, msg: String) { Log.d("$GLOBAL/$tag", msg) }
    actual fun e(tag: String, msg: String, throwable: Throwable?) { Log.e("$GLOBAL/$tag", msg, throwable) }
}
