package dev.koenv.libraryapi

import dev.koenv.libraryapi.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureDI()
    configureDatabase()
    configureMonitoring()
    configureAdministration()
    configureSecurity()
    configureErrorHandling()
    configureRouting()
}
