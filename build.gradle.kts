import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion: String by System.getProperties()

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev770"
}

group = "me.shika"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    val ktorVersion = "2.1.0"

    implementation(compose.desktop.currentOs)

    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("com.github.tkuenneth:nativeparameterstoreaccess:0.1.2")

    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
}

tasks.withType<KotlinCompile>() {
    kotlin.sourceSets.all {
        languageSettings.apply {
            optIn("kotlin.RequiresOptIn")
            optIn("kotlin.time.ExperimentalTime")
            optIn("androidx.compose.ui.ExperimentalComposeUiApi")
        }
    }

    kotlinOptions.jvmTarget = "17"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "InfoMessageImproved"
            packageVersion = version.toString()
        }
    }
}