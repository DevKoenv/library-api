package dev.koenv.libraryapi.plugins

import dev.koenv.libraryapi.domain.repository.UserRepository
import dev.koenv.libraryapi.domain.repository.UserSessionRepository
import dev.koenv.libraryapi.domain.service.AuthService
import dev.koenv.libraryapi.domain.service.SessionService
import dev.koenv.libraryapi.domain.service.UserService
import dev.koenv.libraryapi.storage.repository.UserRepositoryImpl
import dev.koenv.libraryapi.storage.repository.UserSessionRepositoryImpl
import org.koin.dsl.module
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.koin.core.logger.Level

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger(
            (System.getenv("KOIN_LOG_LEVEL") ?: "INFO").uppercase().let {
                when (it) {
                    "DEBUG" -> Level.DEBUG
                    "INFO" -> Level.INFO
                    "WARN", "WARNING" -> Level.WARNING
                    "ERROR" -> Level.ERROR
                    "NONE", "OFF" -> Level.NONE
                    else -> Level.INFO
                }
            }
        )

        modules(
            module {
                // provide Ktor config
                single { environment.config }

                // repositories
                single<UserRepository> { UserRepositoryImpl() }
                single<UserSessionRepository> { UserSessionRepositoryImpl() }

                // services
                single { SessionService(get(), get(), get()) }
                single { AuthService(get(), get(), get()) }
                single { UserService(get()) }
            }
        )
    }
}
