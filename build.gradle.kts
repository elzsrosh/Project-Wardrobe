plugins {
    id("io.gitlab.arturbosch.detekt").version("1.23.0").apply(false)
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.8.21"
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.0")
    }
}
