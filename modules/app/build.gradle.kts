import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.lumo)
}

val APP_NAME = providers.gradleProperty("app.name").get()
val APP_ID = providers.gradleProperty("app.id").get()
val APP_VERSION = providers.gradleProperty("app.version").get()
val APP_VENDOR = providers.gradleProperty("app.vendor").get()
val APP_DESC = providers.gradleProperty("app.description").get()
val APP_COPYRIGHT = providers.gradleProperty("app.copyright").get()
val WIN_UPGRADE_UUID = providers.gradleProperty("app.win.upgradeUuid").get()

fun versionCodeFromSemver(ver: String): Int {
    val parts = ver.split(".").mapNotNull { it.toIntOrNull() }
    val major = parts.getOrNull(0) ?: 0
    val minor = parts.getOrNull(1) ?: 0
    val patch = parts.getOrNull(2) ?: 0
    return major * 1_000_000 + minor * 1_000 + patch
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "app"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Compose Multiplatform core
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                // Shared logic module
                implementation(projects.modules.shared)

                // Lifecycle
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                // Navigation
                implementation(libs.voyager.navigation)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
            }
        }
    }
}

android {
    namespace = APP_ID
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = APP_ID
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionName = APP_VERSION
        versionCode = versionCodeFromSemver(APP_VERSION)

        resValue("string", "app_name", APP_NAME)
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "dev.koenv.libraryapi.app.MainKt"

        val iconsDir = project.layout.projectDirectory.dir("src/jvmMain/resources/icons")

        nativeDistributions {
            packageName = APP_NAME
            packageVersion = APP_VERSION
            description = APP_DESC
            vendor = APP_VENDOR
            copyright = APP_COPYRIGHT

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            includeAllModules = true
            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/jvmMain/resources"))

            windows {
                menu = true
                menuGroup = APP_NAME
                shortcut = true
                perUserInstall = false
                dirChooser = true
                upgradeUuid = WIN_UPGRADE_UUID
                iconFile = iconsDir.file("app.ico")
            }

            macOS {
                bundleID = APP_ID
                iconFile = iconsDir.file("app.icns")
            }

            linux {
                shortcut = true
                iconFile = iconsDir.file("app.png")
            }
        }
    }
}
