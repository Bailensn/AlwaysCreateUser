plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.github.icepony.alwayscreateuser"
    compileSdk = 37
    // API 37 is published as minor versions 37.0/37.1/37.2; 37.2 is the latest stable platform.
    compileSdkMinor = 2

    defaultConfig {
        applicationId = "io.github.icepony.alwayscreateuser"
        // yukihook-runtime-libxposed requires minSdk 26.
        minSdk = 26
        targetSdk = 37
        versionCode = 8
        versionName = "3.0.0"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // YukiHook: core API, libxposed base module, KavaRef bridge and the KSP generator.
    implementation(platform(libs.yukihook.bom))
    implementation(libs.yukihook.core)
    implementation(libs.yukihook.runtime.libxposed)
    implementation(libs.yukihook.extension.kavaref)
    ksp(libs.yukihook.compiler)

    // KavaRef: modern reflection used to resolve UserManagerService members.
    implementation(platform(libs.kavaref.bom))
    implementation(libs.kavaref.core)
    implementation(libs.kavaref.android)
    implementation(libs.kavaref.extension)

    // Provided by the hook framework at runtime, must not be packaged.
    compileOnly(libs.libxposed.api)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.material)
}
