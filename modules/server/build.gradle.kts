import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "dev.koenv.libraryapi.server"
version = "1.0.0"

application {
    mainClass.set("dev.koenv.libraryapi.server.ApplicationKt")
    val isDevelopment = project.hasProperty("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
}

dependencies {
    implementation(projects.modules.shared)

    // --- Ktor Server Core ---
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverHostCommon)
    implementation(libs.ktor.serverDefaultHeaders)
    implementation(libs.ktor.serverCompression)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serverCallLogging)
    implementation(libs.ktor.serverCallId)
    implementation(libs.ktor.serverStatusPages)
    implementation(libs.ktor.serverRequestValidation)
    implementation(libs.ktor.serverResources)
    implementation(libs.ktor.serverMetricsMicrometer)
    implementation(libs.ktor.serverConfigYaml)
    implementation(libs.ktor.serialization.kotlinx.json)

    // --- Authentication ---
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverAuthJwt)
    implementation("de.mkammerer:argon2-jvm:${libs.versions.argon2.get()}")

    // --- Rate Limiting ---
    implementation(libs.ktor.serverRateLimiting)

    // --- Exposed ORM ---
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.migration.core)
    implementation(libs.exposed.migration.jdbc)
    implementation(libs.exposed.kotlin.datetime)

    // --- Database Drivers ---
    implementation(libs.mariadb4j)
    implementation(libs.mariadb.javaClient)

    // --- Connection Pool ---
    implementation(libs.hikariCp)

    // --- Database Migrations ---
    implementation(libs.flyway.core)
    implementation(libs.flyway.mysql)

    // --- Dependency Injection (Koin) ---
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    // --- Metrics & Monitoring ---
    implementation(libs.micrometer.registry.prometheus)

    // --- Logging ---
    implementation(libs.logback)

    // --- Testing ---
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.koin.test.junit5)
}

// Migration generator task
tasks.register<JavaExec>("generateMigrations") {
    group = "database"
    description = "Discover Exposed tables and generate migration SQL scripts"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("dev.koenv.libraryapi.server.storage.db.MigrationGenerator")
}
