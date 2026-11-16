buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // AGP 9 has built-in Kotlin support but pins KGP to 2.2.10.
        // Raising the classpath version is the documented way to use a newer Kotlin release.
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.21")
        // KSP must be aligned with the Kotlin compiler used by the built-in Kotlin support.
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.3.14")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.ksp) apply false
}
